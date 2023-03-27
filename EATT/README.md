# EATT
This is the implementation of "[effort-aware tri-training for semi-supervised JIT defect prediction](https://link.springer.com/chapter/10.1007%2F978-3-030-16145-3_23)".  
It requires [scikit-learn](http://scikit-learn.org/stable/install.html), [numpy](http://www.numpy.org/), [pandas](https://pandas.pydata.org/) and [scipy](https://www.scipy.org/) to run.

# RUN
In terminal
```
python jit_main.py
```
The socre of EATT will output in `score` folder.

# RESULT ANALYZE
In terminal
```
python result_ana.py
```
The mean score and the statistically significant difference will output in `score\stat_info` folder.

