import pandas as pd
import math
import matplotlib.pyplot as plt

df = pd.read_csv('latencyRecord.csv')
s = df['startTime'] + df['latency']

q = s.sort_values()
w = q - q.min()
e = w / 1000.0
e.hist(bins=math.ceil(e.max()))
plt.xlabel('completed time in seconds')
plt.ylabel('throughput per second')
plt.savefig('res.png')
plt.show()
