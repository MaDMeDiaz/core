import random
import string

with open('obf.txt', 'w') as f:
    for i in range(100):
        f.writelines([''.join(random.choice(string.ascii_lowercase)
                     for i in range(20)), '\n'])
