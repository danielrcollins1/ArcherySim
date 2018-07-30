# ArcherySim
Simulate basic archery shooting error

A short and simple program for simulating archery error via a bivariate normal distribution. 
That is: Variation/error in both the x- and y-axes are computed by a random Gaussian variable.

Usage: ArcherySim [precision] [radius] [-L]
  Simulates an archery shooting at target with bivariate normal error model.
  precision is a value modeling shooter accuracy, e.g.:
    1.5 for a shooter with basic-level training
    7.5 for a shooter with grand-master skill
  radius is the radius of the target in feet, e.g.:
    1.5 for a man-sized figure
    2.0 for standard archery target
    12.0 for long-distance clout competition
  Default display is a short table with doubling of ranges;
    -L switch forces long/linear table in 10 foot increments
    
