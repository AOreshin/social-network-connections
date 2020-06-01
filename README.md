# social-network-connections
Program that makes use of weighted union find with path compression algorithm to calculate number of connections needed to connect
all of the users of the network

Takes two parameters: network size and number of iterations

Network size | Iterations | Running time (ms) | Average friend list size | Number of connections
10000000       1000         2394396             8.37                       83700000
1000000        1000         158917              7.21                       7211838
100000         1000         2956                6.04                       604353
10000          1000         262                 4.88                       48850
1000           1000         90                  3.73                       3735

Friend list only gets incremented when network size grows ten times. So it's safe to say that for network of 10 billion people
number of friends to connect everyone will be ~11.

So I'll guess we're all connected! :)
