# ArcherySim
A short and simple program for simulating archery error via a bivariate normal distribution. 
That is: Variation/error in both the x- and y-axes are computed by a random Gaussian variable.

Usage: ArcherySim [precision] [radius] [-L] [-E]
  Simulates an archer shooting at a target with bivariate normal error model.
  precision value models shooter accuracy, e.g.:
    1.0 for accuracy of magic fireball
    1.5 for a archer with basic-level training
    7.5 for a archer with grand-master skill
  radius is the radius of the target in feet, e.g.:
    1.5 for a man-sized figure
    2.0 for standard archery target
    12.0 for long-distance clout competition
  Default display is short table of doubling ranges;
    -L uses long/linear table in 10 yard increments
    -E prints errors for every shot
    
