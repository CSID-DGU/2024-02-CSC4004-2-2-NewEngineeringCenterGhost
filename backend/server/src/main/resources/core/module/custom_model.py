import torch
import torch.nn as nn
import math
if __name__ == '__main__':
    from custom_transformer import TransformerEncoder, TransformerEncoderLayer
else:
    from module.custom_transformer import TransformerEncoder, TransformerEncoderLayer

class PositionalEncoding(nn.Module):
    def __init__(self, d_model, max_len=3584):
        super(PositionalEncoding, self).__init__()
        pe = torch.zeros(max_len, d_model)
        position = torch.arange(0, max_len, dtype=torch.float).unsqueeze(1)
        div_term = torch.exp(torch.arange(0, d_model, 2).float() * (-math.log(10000.0) / d_model))
        pe[:, 0::2] = torch.sin(position * div_term)
        pe[:, 1::2] = torch.cos(position * div_term)

        pe = pe.unsqueeze(0)
        self.register_buffer('pe', pe)
    
    def change_max_len(self, max_len):
        d_model = self.pe.size(2)
        pe = torch.zeros(max_len, d_model)
        position = torch.arange(0, max_len, dtype=torch.float).unsqueeze(1)
        div_term = torch.exp(torch.arange(0, d_model, 2).float() * (-math.log(10000.0) / d_model))
        pe[:, 0::2] = torch.sin(position * div_term)
        pe[:, 1::2] = torch.cos(position * div_term)

        pe = pe.unsqueeze(0).to(self.pe.device)
        self.pe = pe

    def forward(self, x):
        x = x + self.pe[:, :x.size(1), :]
        return x


class Model(nn.Module):
    def __init__(self):
        super(Model, self).__init__()
        self.d_model = 256
        self.pos_encoder = PositionalEncoding(self.d_model)
        self.encoder = TransformerEncoder(
            TransformerEncoderLayer(d_model=self.d_model, nhead=4, dropout=0.1, batch_first=True, activation='gelu'),
            num_layers=8,
            enable_nested_tensor=False
        )
        self.out = nn.Sequential(
            nn.Linear(self.d_model, 1),
        )
    
    def forward(self, x, padding_mask=None):
        if x.size(1) > self.pos_encoder.pe.size(1):
            self.pos_encoder.change_max_len(x.size(1))
        x = self.pos_encoder(x)
        x, _ats = self.encoder(x, src_key_padding_mask=padding_mask)
        x = x[:, 0]
        return self.out(x), _ats

    def set_return_att(self, value: bool):
        self.encoder.set_return_att(value)


if __name__ == "__main__":
    pass