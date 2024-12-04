import numpy as np
import matplotlib.pyplot as plt
import matplotlib.gridspec as gridspec
import os
import sys

class Metrics():
    def __init__(self):
        self.reset()
    
    def reset(self):
        self.tp = 0
        self.fp = 0
        self.tn = 0
        self.fn = 0
        self.prec = []
        self.recall = []
        self.f1 = []
        self.acc = []

    def update(self, pred, target):
        self.tp = ((pred == 1) & (target == 1)).sum().item()
        self.fp = ((pred == 1) & (target == 0)).sum().item()
        self.tn = ((pred == 0) & (target == 0)).sum().item()
        self.fn = ((pred == 0) & (target == 1)).sum().item()

        self.prec.append(self.tp / (self.tp + self.fp + 1e-8))
        self.recall.append(self.tp / (self.tp + self.fn + 1e-8))
        self.f1.append(2 * self.prec[-1] * self.recall[-1] / (self.prec[-1] + self.recall[-1] + 1e-8))
        self.acc.append((self.tp + self.tn) / (self.tp + self.fp + self.tn + self.fn))
    
    def get(self, key):
        return np.mean(getattr(self, key))

class History():
    def __init__(self, metrics_name: list):
        self.metrics_name = metrics_name
        self.history = {'train': {}, 'val': {}}
    
    def update(self, metrics: tuple, mode: str):
        if mode not in self.history:
            raise ValueError(f"Invalid mode: {mode}")

        for i, key in enumerate(self.metrics_name):
            if key not in self.history[mode]:
                self.history[mode][key] = []
            self.history[mode][key].append(metrics[i])
    
    def __getitem__(self, key: str):
        mode, key = key.split('_')
        return self.history[mode][key]
    
    def save(self, path: str): # save png
        fig = plt.figure(figsize=(10, 10))
        gs = gridspec.GridSpec(2, 2, height_ratios=[1, 1], width_ratios=[1, 1], figure=fig)

        ax = plt.subplot(gs[0, :]), plt.subplot(gs[1, 0]), plt.subplot(gs[1, 1])

        for i, key in enumerate(self.metrics_name):
            ax[i].plot(self.history['train'][key], label=f'Train {key}')
            ax[i].plot(self.history['val'][key], label=f'Validation {key}')
            ax[i].set_title(key)
            ax[i].legend()

        plt.savefig(path)

def print_bar(i, total, prefix='', postfix=''):
    c = '='
    bar_length = 40
    progress = int(bar_length * i / total)
    bar = c * progress + '-' * (bar_length - progress)
    print(f'{prefix:>12}[{bar}] {i:7d}/{total:7d} | {postfix}', end='')

def clear_print(cursur_up=0):
    sys.stdout.write('\x1b[2K')
    print('\r', end='')
    for _ in range(cursur_up):
        sys.stdout.write('\x1b[1A')
        sys.stdout.write('\x1b[2K')
        print('\r', end='')

def get_eta(start, now, i, total):
    elapsed = now - start
    eta = elapsed / i * total
    return eta, elapsed 