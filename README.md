# ArcherySim
A short and simple program for simulating archery error via a bivariate normal distribution. 
That is: Variation/error in both the x- and y-axes are computed by a random Gaussian variable.

Usage: ArcherySim [precision] [radius] [-L]<br>
- Simulates an archer shooting at a target with bivariate normal error model.<br>
- precision is a value modeling shooter accuracy, e.g.:<br>
    1.5 for a shooter with basic-level training<br>
    7.5 for a shooter with grand-master skill<br>
- radius is the radius of the target in feet, e.g.:<br>
    1.5 for a man-sized figure<br>
    2.0 for standard archery target<br>
    12.0 for long-distance clout competition<br>
- Default display is a short table with doubling of ranges;<br>
    -L switch forces long/linear table in 10 foot increments<br>
    
