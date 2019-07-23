import random
# import matplotlib.pyplot as plt
from time import time

def gen_choices(seed: random.Random, door):
    a = seed.randint(0, door-1)
    c = [0 for i in range(door)]
    c[a] = 1
    return c

def select(seed: random.Random, door):
    c = seed.randint(0, door-1)
    return c


def reveal_failure(choices, c1, seed, door):
    while True:
        s = seed.randint(0, door-1)
        if s != c1 and choices[s] != 1:
            return s

def select_other(c1, revealed, seed, door):
    while True:
        s = seed.randint(0, door-1)
        if c1 != s and revealed != s:
            return s

def try_monti(d, trial):

    ratio1 = 0
    ratio2 = 0

    r = random.Random()

    for i in range(trial):
        choices = gen_choices(r, d)
        c1 = select(r, d)
        if choices[c1] == 1:
            ratio1 += 1

    # print("変えない勝率: %.1f" % ratio1)

    for i in range(trial):
        choices = gen_choices(r, d)
        c1 = select(r, d)
        n = reveal_failure(choices, c1, r, d)
        c2 = select_other(c1, n, r, d)
        if choices[c2] == 1:
            ratio2 += 1

    # print("変える勝率: %.2f" % ratio2)

    return ratio1, ratio2

def calc_ratios(tri, d):
    r1 = []
    r2 = []
    for i in range(tri):
        ra1, ra2 = try_monti(d, tri)
        r1.append(ra1)
        r2.append(ra2)

    return sum(r1) / len(r1), sum(r2) / len(r2)
    # print("変えない確率: %.2f%%" % (sum(r1) / len(r1)))
    # print("変える確率: %.2f%%" % (sum(r2) / len(r2)))


t = int(input("trial: "))
d = int(input("door: "))
b = time()
calc_ratios(t, d)
f = time()
print(f-b)

# indexes = [i for i in range(3, 11)]
# s1 = []
# s2 = []
#
# for i in range(3, 11):
#     k, l = calc_ratios(100, i)
#     s1.append(k)
#     s2.append(l)
#
# plt.bar(indexes, s2, align="center", label="re-select")
# plt.bar(indexes, s1, align="center", label="stay")
#
# plt.legend()
#
# plt.show()
