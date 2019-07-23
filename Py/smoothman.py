import numpy as np
import matplotlib.pyplot as plt
from scipy.optimize import minimize
def calc(fuel, propl, rocket_k):

    #  g:0 k:1 f:2 l:3 t:4 a:5 h:6
    cons = (
        {'type': 'eq', 'fun': lambda x: ((4*300*x[0]*x[3] - x[0]*(x[1] + x[2] - 4*x[3]*x[4]))
                                        / (x[1] + x[2] - 4*x[3]*x[4])) - x[5]},
        {'type': 'eq', 'fun': lambda x: x[5]*x[4]*x[4]/2 - x[6]},
        {'type': 'eq', 'fun': lambda x: x[0] - 9.81},
        {'type': 'eq', 'fun': lambda x: x[1] - rocket_k},
        {'type': 'eq', 'fun': lambda x: x[2] - fuel},
        {'type': 'eq', 'fun': lambda x: x[3] - propl},
        {'type': 'ineq', 'fun': lambda x: x[4]},
        {'type': 'ineq', 'fun': lambda x: fuel - 4*x[3]*x[4]},
        {'type': 'ineq', 'fun': lambda x: x[6] - 160000},

    )
    zero = np.array([9., 500., 5000., propl, 100., 50., 160000.])
    res = minimize(lambda x: x[4], x0=zero, constraints=cons, method='SLSQP')
    # print('Left Gas:%f' % (res.x[2] - 4*res.x[3]*res.x[4]))
    return res.x[2] - 4*res.x[3]*res.x[4]


def collect_data():
    data = []
    for k in range(500, 99, -20):
        d = []
        for j in range(10, 4, -1):
            cons = (
                {'type': 'ineq', 'fun': lambda x: calc(x, float(j), k)},
                {'type': 'ineq', 'fun': lambda x: x},
            )
            res = minimize(lambda x: calc(x, float(j), k), np.array([5000]), constraints=cons, method='SLSQP')
            print(res)
            if res.success is False:
                d.append(0)
            else:
                d.append(res.x[0])
        data.append(d)

    print(data)


print(calc(5000, 10, 120))