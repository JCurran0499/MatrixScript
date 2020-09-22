# MatrixScape Documentation
## This is the official MatrixScape documentation

MatrixScape is an interpreted command language that supports the calculation of linear algebra
problems. To use MatrixScape, run the [Program.py](https://github.com/JCurran0499/MatrixScape/blob/master/Python/Program.py) file and type in commands.
To exit MatrixScape, type **quit** or **exit** into the prompt.

### What is MatrixScape?

The MatrixScape language allows the user to create variables and interact with them in a linear algebra context. This command language was originally
written in Java, but this new version is written entirely in Python. More features will continuously be added to the language, and this document
will be updated accordingly. 

The purpose of MatrixScape is to allow a simple and straightforward language for calculating problems using linear algebra concepts, such as 
matrices and vectors. This language is meant to make linear algebra problems easier to work with for those who do not have a 
background in linear algebra or machine learning. It is my sincere goal to make complex problems easier and more fun, and I hope MatrixScape
contributes to that effort.

### Introduction to MatrixScape : Data and Variables

There are *5* object data types used in MatrixScape
- **Number:** also referred to as a scalar, a number represents a simple real number value
- **Matrix:** the matrix is a mathematical two-dimensional organization of numbers. Matrices made of one column are *vectors*
- **Boolean:** the computer science representation of a true/false value
- **Tuple:** a collection of MatrixScape objects/variables in a given order
- **Range:** a discrete range of integers from a distinct startpoint and endpoint

The most efficient way to interact with these objects is to store them in *variables*
```
>> x = 5;
>> y = 10;
>> (x + y) * 3
45
```
*Tip: ending a MatrixScape calculation or variable declaration with __;__ will not cause the result to be printed immediately, which is the default*
*Tip: comments in MatrixScape begin with __//__. Any text after that on a given line will be ignored*

Variables can be set to the 5 data types, and nothing else. Variables are dynamically typed and can be reset to any data type.
```
>> num = 0.5   // number
num = 0.5

>> mat = [1 2 3; 4 5 6]   // matrix
mat =
  [ 1  2  3 ]
  [ 4  5  6 ]
  
>> bool = true  // boolean
bool = true

>> tup = num, num+1, bool, mat   // tuple
tup = 0.5, 1.5, true, [ 1  2  3  ;  4  5  6 ]

>> ran = 1:6  // range (can only consist of integers)
ran = 1:6
```
