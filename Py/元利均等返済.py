from scipy.optimize import minimize_scalar
import matplotlib.pyplot as plt


def calc(debt, term, monthly_payment, interest, deposit=0):

    debt -= deposit
    returned = []
    interest_returned = []

    for i in range(term*12):
        interest_returned.append(debt*interest/12)
        returned.append(monthly_payment - interest_returned[i])
        debt = debt - returned[i]

    return debt

def calc_bonus(debt, term, monthly_payment, interest):

    returned = []
    interest_returned = []

    for i in range(term*2):
        interest_returned.append(debt*interest/2)
        returned.append(monthly_payment - interest_returned[i])
        debt = debt - returned[i]

    return debt

def showData(debt, term, monthly_payment, interest, deposit=0):
    d = debt
    debt -= deposit
    returned = []
    interest_returned = []

    for i in range(term * 12):
        interest_returned.append(debt * interest / 12)
        returned.append(monthly_payment - interest_returned[i])
        debt = debt - returned[i]

    plt.plot(interest_returned, label="Interest")
    plt.plot(returned, label="Principal")

    plt.plot([i + j for i, j in zip(returned, interest_returned)], label="Summary")

    plt.legend()
    plt.show()

    allr = sum(returned) + sum(interest_returned)

    return debt, allr


d = int(input("負債金額:"))
t = int(input("返済期間(年):"))
it = float(input("利息:"))
bonus = int(input("ボーナス返済:"))
rlt = minimize_scalar(lambda x: abs(calc(debt=d-bonus, term=t, monthly_payment=x, interest=it) - t))
rltb = minimize_scalar(lambda x: abs(calc_bonus(debt=bonus, term=t, monthly_payment=x, interest=it) - t))

tmp, allr = showData(debt=d, term=t, monthly_payment=rlt.x, interest=it)
if tmp >= 0:
    rlt.x += 1
elif tmp <= 0:
    rlt.x -= 1
if rltb.x >= 0:
    rltb.x += 1
elif rltb.x <= 0:
    rlt.x -= 1
print('毎月の返済額: %d円' % rlt.x)
print('最終月の返済額: %d円' % (allr - ((12*t-1)*rlt.x)))
print(rltb.x)

