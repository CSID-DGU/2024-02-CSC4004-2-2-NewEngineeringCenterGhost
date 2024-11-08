import torch
import torch.nn as nn
import math

class PositionalEncoding(nn.Module):
    def __init__(self, d_model, max_len=5000):
        super(PositionalEncoding, self).__init__()
        pe = torch.zeros(max_len, d_model)
        position = torch.arange(0, max_len, dtype=torch.float).unsqueeze(1)
        div_term = torch.exp(torch.arange(0, d_model, 2).float() * (-math.log(10000.0) / d_model))
        pe[:, 0::2] = torch.sin(position * div_term)
        pe[:, 1::2] = torch.cos(position * div_term)

        pe = pe.unsqueeze(0)
        self.register_buffer('pe', pe)

    def forward(self, x):
        x = x + self.pe[:, :x.size(1), :]
        return x
    
class GlobalAvgPoolingWithMask(nn.Module):
    def __init__(self):
        super(GlobalAvgPoolingWithMask, self).__init__()
        
    def forward(self, x: torch.Tensor, mask: torch.Tensor):
        mask = mask.unsqueeze(-1)
        x_masked = x * mask
        sum_x = x_masked.sum(dim=1)
        sum_mask = mask.sum(dim=1)
        sum_mask = sum_mask.clamp(min=1e-6)
        avg = sum_x / sum_mask
        return avg
        

class Model(nn.Module):
    def __init__(self):
        super(Model, self).__init__()
        self.d_model = 256
        self.pos_encoder = PositionalEncoding(self.d_model)
        self.encoder = nn.TransformerEncoder(
            nn.TransformerEncoderLayer(d_model=self.d_model, nhead=4, dropout=0.12),
            num_layers=8,
        )
        self.pooling = GlobalAvgPoolingWithMask()
        self.out = nn.Sequential(
            nn.Flatten(),
            nn.Linear(self.d_model, 1),
        )
    
    def forward(self, x, padding_mask):
        x = self.pos_encoder(x).transpose(0, 1)
        x = self.encoder(x, src_key_padding_mask=padding_mask).transpose(0, 1)
        x = self.pooling(x, padding_mask)
        return self.out(x)
        

if __name__ == "__main__":
    device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
    model = Model().to(device)
    print(model)
    print(sum(p.numel() for p in model.parameters() if p.requires_grad))
    x = torch.randn(32, 128, 256, device=device, dtype=torch.float16)
    padding_mask = torch.zeros(32, 128, dtype=torch.bool, device=device)
    padding_mask[0, 64:] = True
    print(model(x, padding_mask).shape)