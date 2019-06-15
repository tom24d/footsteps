import random
import time
from tqdm import tqdm
import matplotlib.pyplot as plt

def zyanManual():
    print("何回戦行いますか？")
    game = int(input('数字:'))
    win = 0
    lose = 0
    draw = 0
    playerHands = []
    analyzedHands = []
    index = 0
    gameRes = []
    while True:
        print(index + 1, "回戦")
        print('\n最初はグー・・・')
        print('じゃんけん・・\n')
        try:
            you = int(input('1:グー\n2:チョキ\n3:パー\n'))
        except:
            print("もう一度入力してください")
            continue

        if you == -1:
            exit(0)

        if index < game/3:
            com = random.randint(1, 3)
        else:
            com = analyzedHands[random.randint(0, index)]

        if you==1:
            analyzedHands = analyzedHands + [2]
        else:
            analyzedHands = analyzedHands + [you-1]
        playerHands = playerHands + [you]
        if index >=2:
            if playerHands[index-1]==playerHands[index-2]:
                before = playerHands[index-1]
                com = before + 1
                if com == 4:
                    com = 1



        print('\nあなた：' + getHandName(you) + '\nコンピュータ：' + getHandName(com))

        r = (com - you + 3) % 3

        if r == 0:
            print('\n引き分け！\nもう一回!')
            draw += 1

        elif r == 2:
            print('\nあなたの負け..')
            lose += 1
            index += 1
            gameRes = gameRes + [win / index * 100]

        else:
            print('\nあなたの勝ち!')
            win += 1
            index += 1
            gameRes = gameRes + [win / index * 100]

        time.sleep(0.1)
        if index == game:
            break

    return win/game*100, win, lose, draw, gameRes

def zyanAuto():
    print("何回戦行いますか？")
    game = int(input('数字:'))
    win = 0
    lose = 0
    draw = 0
    index = 0
    war = tqdm(total=game)
    gameRes = []
    while True:
        you = random.randint(1, 3)
        com = random.randint(1, 3)

        r = (com - you + 3) % 3

        if r == 0:
            draw += 1

        elif r == 2:
            lose += 1
            index += 1
            war.update(1)
            gameRes = gameRes + [win / index * 100]

        else:
            win += 1
            index += 1
            war.update(1)
            gameRes = gameRes + [win / index * 100]

        if index == game:
            break


    war.close()
    return win/game*100, win, lose, draw, gameRes

def getHandName(hand):
    if hand == 1:
        return "グー"
    elif hand == 2:
        return "チョキ"
    else:
        return "パー"


mode = int(input('入力モード?\n1:マニュアル\n2:オート\n'))
ratio = 0

if mode == 1:
    ratio, win, lose, draw, g = zyanManual()
else:
    ratio, win, lose, draw, g = zyanAuto()

print('\n'+str(win)+'勝'+str(lose)+'敗'+str(draw)+'引き分け')
print("これで終了です。あなたの勝率は", round(ratio, 1), "%です。")

plt.plot(range(len(g)), g)
plt.show()