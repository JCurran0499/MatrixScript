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
*Tip: ending a MatrixScape variable declaration with __;__ will not cause the result to be printed immediately, which is the default*
*Tip: comments in MatrixScape begin with __//__. Any text after that on a given line will be ignored*

Variables can be set to the 5 data types, and nothing else. Variables are dynamically typed and can be reset to any data type.
Variable names can only consist of letters, numbers, and underscores, and they cannot share names with any existing MatrixScape functions.
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
*Tip: ranges must consist of integers, but they can include negative integers. If the first integer is greater than the second, the range will run in reverse order*

Any MatrixScape syntax errors will be caught and the given command will not run. The rest of the program will be unaffected
```
>> x = 5;
>> y = true;
>> x + y
Error: invalid addition
```

### Matrix and Vector Calculations

Matrices are created using the format **\[ row 1 values ; row 2 values ; ... ; row n values]**, with the values in each row separated by spaces. Matrices that do not 
have an equal number of values in each row will result in an error.

Functions involving matrices are the core of MatrixScape. Simple mathematical operations (+, -, \*, /, ^) work between matrices and numbers.
```
>> m = [1 1 1 ; 4  5 7; 8 9 0];
>> m * 3
[  3   3   3 ]
[ 12  15  21 ]
[ 24  27   0 ]

>> m + [1 1 1; 2 2 2; 3 3 3]
[  2   2  2 ]
[  6   7  9 ]
[ 11  12  3 ]

>> -m
[ -1  -1  -1 ]
[ -4  -5  -7 ]
[ -8  -9   0 ]
```
*Tip: multiplying matrices will calculate the dot product*

It is also import to retrive values from matrices. This is done using the **get** command using the format
**get _row_:_column_ from _matrix_**. Indexing begins at 0.
```
>> m = [5 6 7 8; 9 0 0 -(5 + 6)]
m = 
  [ 5  6  7    8 ]
  [ 9  0  0  -11 ]
  
>> g = get 0:2 from m
g = 7

>> (get 1:3 from m) ^ (get 0:0 from m)
-161051
```
To get whole rows and columns, use the format **get row _n_ from m** or **get col _n_ from m**. Use a range instead of *n* to get multiple rows and columns.

Using the format **get _n_ from _tuple_** retrieves a value from a tuple.

## Functions

There are several important functions used with MatrixScape objects. This documentation has already discussed the **get** function.
- **row _matrix_**: returns the number of rows in the matrix
- **col _matrix_**: returns the number of columns in the matrix
- **size _matrix/tuple_**: returns the number of total elements in the matrix or tuple
- **not _boolean_**: returns the opposite of the given boolean
- **_boolean_ and _boolean_**: returns the boolean of whether both booleans are *true*
- **_boolean_ or _boolean_**: returns the boolean of whether at least one boolean is *true*
- **square? _matrix_**: returns the boolean of whether the matrix is square
- **invertible? _matrix_**: returns the boolean of whether the matrix is invertible
- **symmetrical? _matrix_**: returns the boolean of whether the matrix is symmetrical
- **inverse _matrix_**: returns the inverse of the matrix (error if not invertible)
- **transpose _matrix_**: returns the transpose of the matrix
- **rref _matrix_**: returns the *reduced row echelon form* of the matrix
- **det _matrix_**: returns the number of the determinant of the matrix
- **rank _matrix_**: returns the number of the rank of the matrix
- **identity _number_**: returns an *identity matrix* of the given dimensions
- **zero vector _number_**: returns a *zero vector* of the given length
- **type _object_**: prints the data type of the object (prints "void" if no type and "error" if error)
- **list**: lists all current variables, organized by data type
*Tip: any function that ends with __?__ will return a boolean*

Here are some examples
```
>> t = true;
>> f = false;
>> b = not (true and false)
b = true

>> identity 3
[ 1  0  0 ]
[ 0  1  0 ]
[ 0  0  1 ]

>> type b
boolean

>> type (x = 5)
void

>> m1 = [1 2 3 4 5; 6 7 8 9 0];
>> rref m1
[ 1  0  -1  -2  -7 ]
[ 0  1   2   3   6 ] 

>> rank m1
2
```
