
import matplotlib.pyplot as plt
import random
import datetime as dte
import matplotlib.dates as mdates
import numpy as np

class DataCrawler:
    def __init__(self, openTime, closeTime):
        self.openTime = openTime
        self.closeTime = closeTime
        self.que_data = []
        self.time_data = []
        self.value = 0

    def output(self, lengthOfQue, dtime):
        self.que_data.extend([lengthOfQue])
        self.time_data.extend([dtime])

    def notify_sales(self, value):
        self.value += value

    def showGraph(self):
        None
        ###plt.plot(self.time_data, self.que_data)
        ###plt.show()

class StoreManager:
    def __init__(self, limit, openTime, closeTime, cons_fac, value=950):
        self.limit = limit
        self.openTime = openTime
        self.closeTime = closeTime
        self.consumers = cons_fac
        self.current_time = openTime
        self.eating_consumers = []
        self.waiting_consumers = []
        self.ramen_value = value
        self.former_limit = limit
        self.peek = False

    def seats_is_full(self):
        if len(self.waiting_consumers) >= 8:
            if not self.peek:
                self.peek = True
                self.former_limit = self.limit
                self.limit = 14
        if len(self.waiting_consumers) <= 9:
            if self.peek:
                self.peek = False
                self.limit = self.former_limit
        return len(self.eating_consumers) >= self.limit

    def receiveConsumer(self, consumer):
        if(self.seats_is_full()):
            self.waiting_consumers = self.waiting_consumers + [consumer]
        else:
            self.consumers.extend(consumer)

    def init_simus(self):
        data = DataCrawler(self.openTime, self.closeTime)
        while True:
            for eatinger in self.eating_consumers:
                if eatinger.time_to_finish == self.current_time:
                    self.eating_consumers.remove(eatinger)
                    data.notify_sales(self.ramen_value)
            for consumer in self.consumers:
                if(consumer.comingDate == self.current_time):
                    self.waiting_consumers += [consumer]
                    self.consumers.remove(consumer)

            for waitinger in self.waiting_consumers:
                if not self.seats_is_full():
                    waitinger.start_eat(self.current_time)
                    self.eating_consumers += [waitinger]
                    self.waiting_consumers.remove(waitinger)
            data.output(len(self.waiting_consumers), self.current_time)

            self.current_time += dte.timedelta(minutes=1)
            if(self.current_time == self.closeTime + dte.timedelta(minutes=1)):
                break

        ###data.showGraph()
        return data


class ConsumerFactory:
    def __init__(self, numPerTenMin):
        self.ratio = numPerTenMin
        self.rand = random.Random()

    def get_simulation_data(self):
        consumers = []
        con = []
        for i in range(6*4):
            for j in range(self.ratio):
                ###con.extend([Consumer(self.rand.randint(0, 9) + i * 10, 17)])
                con.extend([Consumer(self.rand.randint(0, 9)+i*10, self.rand.randint(10, 24))])
            consumers.extend(sorted(con, key=lambda x: x.comingDate))
            con = []

        self.cons = consumers
        return StoreManager(8, dte.datetime(2019, 5, 5, 11, 30, 0), dte.datetime(2019, 5, 5, 15, 0, 0), self.cons).init_simus()

class Consumer:
    def __init__(self, comingDate, timeToFinish):
        self.comingDate = dte.datetime(2019, 5, 5, 11, 0, 0) + dte.timedelta(minutes=comingDate)
        self.timeToFin = timeToFinish

    def start_eat(self, time_to_start):
        self.time_to_finish = time_to_start + dte.timedelta(minutes=self.timeToFin)

def show_gross(gross, j):
    print('Estimated Earnings if %d people in 10min comes: ¥%d' % (j, gross))


data = []
date_data = []
gross = 0
for j in range(6, 11):
    for i in range(1000):
        gene = ConsumerFactory(j)
        d = gene.get_simulation_data()
        data += [d.que_data]
        date_data = d.time_data
        gross += d.value
    data = [np.average(x) for x in np.asarray(data).T.tolist()]
    gross = gross / 1000
    show_gross(gross, j)
    plt.plot(date_data, data, label=str(j))
    data = []
    gross = 0

plt.xlabel("time")
plt.ylabel("length of queue")
plt.legend()
plt.gca().xaxis.set_major_formatter(mdates.DateFormatter('%H:%M'))
plt.show()


'''

data = ConsumerFactory(5).get_simulation_data()
plt.plot(data.time_data, data.que_data)
plt.xlabel("time")
plt.ylabel("length of queue")
plt.gca().xaxis.set_major_formatter(mdates.DateFormatter('%H:%M'))
plt.show()


for j in range(3, 11):
    data = ConsumerFactory(j).get_simulation_data()
    plt.plot(data.time_data, data.que_data, label=str(j))
plt.xlabel("time")
plt.ylabel("length of queue")
plt.legend()
plt.gca().xaxis.set_major_formatter(mdates.DateFormatter('%H:%M'))
plt.show()
'''