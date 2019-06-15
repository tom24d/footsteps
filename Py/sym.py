from scipy.optimize import minimize
import numpy as np
import matplotlib.pyplot as plt


def calc(river):

    cons = (
        {'type': 'eq', 'fun': lambda x: x[0] ** 2 + x[1] ** 2 - 1.11 ** 2},
        {'type': 'eq', 'fun': lambda x: x[2] ** 2 + x[3] ** 2 - 7 ** 2},
        {'type': 'eq', 'fun': lambda x: x[0] * x[4] - 40},
        {'type': 'eq', 'fun': lambda x: x[2] * x[5] - 130},
        {'type': 'eq', 'fun': lambda x: x[1] * x[4] + (x[3] - river) * x[5] - 310}
    )
    zero = np.array([1., 1., 1., 1., 1., 1.])

    res = minimize(lambda x: x[4] + x[5], x0=zero, constraints=cons, method='SLSQP')
    return res



river = float(input("river(m/s) = "))


res = calc(river)



print('Py=', res.x[1]*res.x[4])
print('S=', res.x[4]+res.x[5])

for i in range(10):
    print(i)
    x = np.array([0])
    y = np.array([0])
    rlt = calc(i)
    y= np.append(y,rlt.x[1]*rlt.x[4])
    y=np.append(y,310)
    x=np.append(x,rlt.x[0]*rlt.x[4])
    x=np.append(x,170)
    plt.plot(x,y)

plt.grid(True)
plt.xlabel("x axis")
plt.ylabel("y axis")

plt.show()