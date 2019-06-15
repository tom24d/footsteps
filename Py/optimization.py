import numpy as np

def calc(f, X):
    h = 1e-4
    grad = np.zeros_like(X)

    for i in range(X.size):
        store_x = X[:]

        X[i] += h
        add = f(X)

        X = store_x[:]

        X[i] -= h
        subs = f(X)

        grad[i] = (add-subs)/(2*h)

    return grad

def gradient(f, X, lr, max):

    for i in range(max):
        X -= (lr*calc(f,X))

    print("[{:3d}] X = {}, f(X) = {:.7f}".format(1000, X, f(X)))

    return X

f = lambda x: np.sin(x[0]) + x[0] - 1
x = np.array([1.0])

gradient(f, x, 0.0003999, 1426)