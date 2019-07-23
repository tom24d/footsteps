import matplotlib.pyplot as plt
from tqdm import tqdm
import func as funcs


def input_params(s):
    return int(input(s))


def input_fuel():
    return 8

def calc_speed(rk, f, cfc):
    h = 0
    G = 9.81
    v = [0]  # 速度　履歴
    rocket_k = rk
    fuel = []  # 燃料 残り履歴
    mass_rocket = []  # 総質量　履歴
    # a = input_params("How much fuel per one engine(kg)?")
    a = f
    fuel.append(a)
    mass_rocket = mass_rocket + [fuel[0] + rocket_k]
    # print("Lift Off!!")
    T = 0

    cf = cfc

    while True:
        # using_gas = input_params("T+" + str(T) + ", E=")
        # using_gas = input_fuel()
        using_gas = cf[T]
        if 4 * using_gas > fuel[T]:
            print("Run out of gas. Launch is failed.")
            print('v=%.3fkm' % (v[T] / 1000))
            print('T+%d' % T)
            print("h=%.3f" % (h/1000))
            # plt.plot(list(range(0, T+1)), [x/1000 for x in v], label=str(ug)+":failed")
            return

        fuel.append(fuel[T] - 4 * using_gas)
        mass_rocket.append(mass_rocket[T] - 4 * using_gas)
        mass = (mass_rocket[T] + mass_rocket[T + 1]) / 2
        # print('Propulsion: %d m/s2' % int(((4 * 300 * G * using_gas - G * mass) / mass) + v[T]))
        v.append(((4 * 300 * G * using_gas - G * mass) / mass) + v[T])
        h += (v[T] + v[T + 1]) / 2
        # print("up to " + str(round(h / 1000, 3)) + "km")
        # print("Left Gas: " + str(mass_rocket[T + 1] - rocket_k) + "kg")
        if h >= 160000:

            print("Over 160km high.")
            print('T+%d' % T)
            print('v=%.3fkm' % (v[T + 1] / 1000))
            # plt.plot(list(range(0, T+2)), [x/1000 for x in v], label=str(ug))
            return v[len(v)-1]/1000

        if h < 0:
            print("cannot get propulsion. Launch failed.")
            print('T+%d' % T)
            print("h=%.3f" % (h/1000))

            # plt.plot(list(range(0, T+2)), [x/1000 for x in v], label=str(ug)+":failed")
            return

        T += 1


# print("func1")
# print(calc_speed(500, 5000,  [funcs.ten(xx) for xx in range(0, 500)]))
# print("func2")
# print(calc_speed(500, 5000,  [funcs.seven(xx) for xx in range(0, 500)]))
# print("func3")
# print(calc_speed(500, 5000,  [funcs.func3(xx) for xx in range(0, 500)]))
# print("func4")
# print(calc_speed(500, 5000,  [funcs.func4(xx) for xx in range(0, 500)]))

# plt.plot(list(range(0, T+2)), v)
# plt.show()

# minmax = []
# for fuel in tqdm(range(2000, 5000)):
#     for rk in range(500, 100, -1):
#         rlt = calc_speed(rk, fuel, [funcs.func3(xx) for xx in range(0, 500)])
#         if rlt is None:
#             continue
#         else:
#             minmax.append((rlt, fuel, rk))
#             break
#
# minmax = [(r, f, rk) for r, f, rk in minmax if r >= 7.9]
# minmax = sorted(minmax, key=lambda x: x[1])
# minmax = sorted(minmax, key=lambda x: x[2], reverse=True)
# print(minmax[0])

# plt.legend()
# plt.show()


