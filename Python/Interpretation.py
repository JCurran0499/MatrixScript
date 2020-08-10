from MatrixPackage import *

class Interpretation():
	__max_prec = 0
	
	def __init__(self):
		raise TypeError("abstract class")
		
	def __str__(self):
		raise TypeError("not implemented")
		
	def solve(self, vars):
		raise TypeError("not implemented")
			


class Err(Interpretation):
	def __init__(self, message):
		self.message = message
		
	def __str__(self):
		return "Error: " + self.message
		
	def solve(self, vars):
		return self
		
		
class Mat(Interpretation):
	def __init__(self, i):
		self.i = i
		
	def __str__(self):
		return str(self.i)
		
	def __eq__(self, m):
		if not type(m) is Mat:
			return Bool(False)
			
		return Bool(self.i == m.i)
		
	def size(self):
		return self.i.size()
		
	def __neg__(self):
		return Mat(-(self.i))
		
	def solve(self, vars):
		if self.i is None:
			return Err("invalid matrix")
			
		return self
		
		
class Num(Interpretation):
	def __init__(self, i):
		self.i = Precision.decimal(i, 5)
		
	def __str__(self):
		return str(Precision.num(self.i))
		
	def __float__(self):
		return float(self.i)
		
	def __int__(self):
		return int(self.i)
		
	def __eq__(self, n):
		if not type(n) is Num:
			return Bool(False)
			
		return Bool(Precision.num(self.i) == Precision.num(n.i))
		
	def __lt__(self, n):
		if not type(n) is Num:
			return Err("invalid comparison")
			
		return Bool(Precision.num(self.i) < Precision.num(n.i))
		
	def __gt__(self, n):
		if not type(n) is Num:
			return Err("invalid comparison")
			
		return Bool(Precision.num(self.i) > Precision.num(n.i))
		
	def __le__(self, n):
		if not type(n) is Num:
			return Err("invalid comparison")
			
		return Bool(Precision.num(self.i) <= Precision.num(n.i))
		
	def __ge__(self, n):
		if not type(n) is Num:
			return Err("invalid comparison")
			
		return Bool(Precision.num(self.i) >= Precision.num(n.i))
		
	def __neg__(self):
		return Num(-(Precision.num(self.i)))
		
	def solve(self, vars):
		return self


class Add(Interpretation):
	def __init__(self, i1, i2):
		self.i1 = i1
		self.i2 = i2
		
	def __str__(self):
		return str(self.i1) + " + " + str(self.i2)
		
	def solve(self, vars):
		i1 = self.i1.solve(vars)
		i2 = self.i2.solve(vars)
		
		if type(i1) is Err:
			return i1
		if type(i2) is Err:
			return i2
			
		if type(i1) is Mat and type(i2) is Mat:
			return Mat(i1.i + i2.i)
		elif type(i1) is Num and type(i2) is Num:
			return Num(i1.i + i2.i)
		else:
			return Err("invalid addition")
		
		
class Sub(Interpretation):
	def __init__(self, i1, i2):
		self.i1 = i1
		self.i2 = i2
		
	def __str__(self):
		return str(self.i1) + " - " + str(self.i2)
		
	def solve(self, vars):
		i1 = self.i1.solve(vars)
		i2 = self.i2.solve(vars)
		
		if type(i1) is Err:
			return i1
		if type(i2) is Err:
			return i2
			
		if type(i1) is Mat and type(i2) is Mat:
			return Mat(i1.i - i2.i)
		elif type(i1) is Num and type(i2) is Num:
			return Num(i1.i - i2.i)
		else:
			return Err("invalid subtraction")
		
		
class Mult(Interpretation):
	def __init__(self, i1, i2):
		self.i1 = i1
		self.i2 = i2
		
	def __str__(self):
		return str(self.i1) + " * " + str(self.i2)
		
	def solve(self, vars):
		i1 = self.i1.solve(vars)
		i2 = self.i2.solve(vars)
		
		if type(i1) is Err:
			return i1
		if type(i2) is Err:
			return i2
			
		if type(i1) is Num and type(i2) is Num:
			return Num(i1.i * i2.i)
		elif type(i1) is Num and type(i2) is Mat:
			return Mat(i2.i * i1.i)
		elif (type(i1) is Mat and type(i2) is Mat) or (type(i1) is Mat and type(i2) is Num):
			return Mat(i1.i * i2.i)
		else:
			return Err("invalid multiplication")		
		
		
class Div(Interpretation):
	def __init__(self, i1, i2):
		self.i1 = i1
		self.i2 = i2
		
	def __str__(self):
		return str(self.i1) + " / " + str(self.i2)
		
	def solve(self, vars):
		i1 = self.i1.solve(vars)
		i2 = self.i2.solve(vars)
		
		if type(i1) is Err:
			return i1
		if type(i2) is Err:
			return i2
		
		if type(i2) is Num and Precision.num(i2.i) == 0:
				return Err("cannot divide by zero")
				
		if type(i1) is Mat and type(i2) is Num:
			return Mat(i1.i / i2.i)
		elif type(i1) is Num and type(i2) is Num:
			return Num(i1.i / i2.i)
		else:
			return Err("invalid division")
		
		
class Pow(Interpretation):
	def __init__(self, i1, i2):
		self.i1 = i1
		self.i2 = i2
		
	def __str__(self):
		return str(self.i1) + " ^ " + str(self.i2)
		
	def solve(self, vars):
		i1 = self.i1.solve(vars)
		i2 = self.i2.solve(vars)
		
		if type(i1) is Err:
			return i1
		if type(i2) is Err:
			return i2
			
		if type(i1) is Num and type(i2) is Num:
			return Num(i1.i ** i2.i)
		elif type(i1) is Mat and is_integer(i2) and Precision.num(i2.i) >= 0:
			return Mat(i1.i ** i2.i)
		else:
			return Err("invalid exponent")
		
		
class Compare(Interpretation):
	def __init__(self, operator, i1, i2):
		self.__operator = operator
		self.i1 = i1
		self.i2 = i2
		
	def operator(self):
		return self.__operator
		
	def __str__(self):
		return str(self.i1) + " " + self.__operator + " " + str(self.i2)
		
	def solve(self, vars):
		i1 = self.i1.solve(vars)
		i2 = self.i2.solve(vars)
		
		if type(i1) is Err:
			return i1
		if type(i2) is Err:
			return i2
			
		if self.operator == "==":
			if not (type(i1) in [Mat, Num, Bool, Range, Tuple] and type(i2) in [Mat, Num, Bool, Range, Tuple]):
				return Err("invalid comparison")
				
			return i1 == i2
			
		elif self.operator == "<=":
			if not type(i1) in [Num, Range]:
				return Err("invalid comparison")
				
			return i1 <= i2
			
		elif self.operator == ">=":
			if not type(i1) in [Num, Range]:
				return Err("invalid comparison")
				
			return i1 >= i2
			
		elif self.operator == "<":
			if not type(i1) in [Num, Range]:
				return Err("invalid comparison")
				
			return i1 < i2
			
		elif self.operator == ">":
			if not type(i1) in [Num, Range]:
				return Err("invalid comparison")
				
			return i1 > i2
			
		elif self.operator == "!=":
			if not type(i1) in [Num, Range]:
				return Err("invalid comparison")
				
			return not i1 == i2
			
		else:
			return Err("invalid command")
		
		
class Declare(Interpretation):
	def __init__(self, name, i):
		self.name = name
		self.i = i
		
	def name(self):
		return self.__name
		
	def __str__(self):
		return self.__name + " = " + str(self.i)
		
	def solve(self, vars):
		i = self.i.solve(vars)
		
		if type(i) is Err:
			return i
			
		if not valid_name(self.name):
			return Err("invalid variable name")
			
		if not type(i) in [Mat, Num, Bool, Range, Tuple]:
			return Err("invalid declaration")
			
		return Declare(self.name, i)
		
		
class Var(Interpretation):
	def __init__(self, name):
		self.name = name
		
	def __str__(self):
		return self.name
		
	def solve(self, vars):
		if not self.name in vars:
			return Err('variable "{}" does not exist'.format(self.name))
			
		return vars[self.name]
		
		
class Bool(Interpretation):
	def __init__(self, i):
		self.i = i
		
	def __str__(self):
		if self.i:
			return "true"
		else:
			return "false"
			
	def __eq__(self, b):
		return Bool(self.i == b.i)
			
	def solve(self, vars):
		return self
		
		
class Rows(Interpretation):
	def __init__(self, i):
		self.i = i
		
	def __str__(self):
		return "rows " + str(self.i)
		
	def solve(self, vars):
		i = self.i.solve(vars)
		
		if type(i) is Err:
			return i
			
		if not type(i) is Mat:
			return Err("must use matrix")
			
		return Num(i.i.rows())
		

class Cols(Interpretation):
	def __init__(self, i):
		self.i = i
		
	def __str__(self):
		return "cols " + str(self.i)
		
	def solve(self, vars):
		i = self.i.solve(vars)
		
		if type(i) is Err:
			return i
			
		if not type(i) is Mat:
			return Err("must use matrix")
			
		return Num(i.i.cols())
		

class Size(Interpretation):
	def __init__(self, i):
		self.i = i
		
	def __str__(self):
		return "size " + str(self.i)
		
	def solve(self, vars):
		i = self.i.solve(vars)
		
		if type(i) is Err:
			return i
			
		if not type(i) in [Mat, Tuple, Range]:
			return Err("must use matrix, tuple, or range")
			
		return Num(i.size())
		
		
class Square(Interpretation):
	def __init__(self, i):
		self.i = i
		
	def __str__(self):
		return "square? " + str(self.i)
		
	def solve(self, vars):
		i = self.i.solve(vars)
		
		if type(i) is Err:
			return i
			
		if not type(i) is Mat:
			return Err("must use matrix")
			
		return Bool(i.i.is_square())
		
		
class Symmetrical(Interpretation):
	def __init__(self, i):
		self.i = i
		
	def __str__(self):
		return "symmetrical? " + str(self.i)
		
	def solve(self, vars):
		i = self.i.solve(vars)
		
		if type(i) is Err:
			return i
			
		if not type(i) is Mat:
			return Err("must use matrix")
			
		return Bool(i.i.is_symmetrical())
		
		
class Set(Interpretation):
	def __init__(self, i1, i2, i3):
		self.i1 = i1
		self.i2 = i2
		self.i3 = i3
		
	def __str__(self):
		return "set " + str(self.i1) + " from " + str(self.i2) + " to " + str(self.i3)
		
	def solve(self, vars):
		i3 = self.i3.solve(vars)
		
		if type(self.i1) is Err:
			return self.i1
		if type(self.i2) is Err:
			return self.i2
		if type(i3) is Err:
			return i3
			
		return Set(self.i1, self.i2, i3)
		
		
class Get(Interpretation):
	def __init__(self, i1, i2):
		self.i1 = i1
		self.i2 = i2
		
	def __str__(self):
		return "get " + str(self.i1) + " from " + str(self.i2)
		
	def solve(self, vars):
		i2 = self.i2.solve(vars)
		
		if type(self.i1) is Err:
			return self.i1
		if type(i2) is Err:
			return i2
		
		if type(i2) is Mat:
			if type(self.i1) is Rows:
				i1 = self.i1.i.solve(vars)
				
				if type(i1) is Err:
					return i1
				
				if is_integer(i1):
					m = i2.i.get_row(Precision.num(i1))
					if m is None:
						return Err("out of bounds")
					
					return Mat(m)
					
				else:
					return Err("invalid request")
					
			elif type(self.i1) is Cols:
				i1 = self.i1.i.solve(vars)
				
				if type(i1) is Err:
					return i1
				
				if is_integer(i1):
					m = i2.i.get_column(Precision.num(i1))
					if m is None:
						return Err("out of bounds")
					
					return Mat(m)
				else:
					return Err("invalid request")
					
			elif type(self.i1) is Range:
				i1 = self.i1.solve(vars)
				
				if type(i1) is Err:
					return i1
					
				n = i2.i.get_value(Precision.num(i1.i1), Precision.num(i1.i2))
				if n is None:
					return Err("out of bounds")
				
				return Num(n)
				
			else:
				return Err("invalid request")
				
		elif type(i2) is Tuple:
			i1 = self.i1.solve(vars)
			
			if type(i1) is Err:
				return i1
				
			if is_integer(i1):
				return i2[Precision.num(i1)]
			else:
				return Err("invalid request")
				
		else:
			return Err("invalid request")
		
		
class Transpose(Interpretation):
	def __init__(self, i):
		self.i = i
		
	def __str__(self):
		return "transpose " + str(self.i)
		
	def solve(self, vars):
		i = self.i.solve(vars)
		
		if type(i) is Err:
			return i
			
		if not type(i) is Mat:
			return Err("must use matrix")
			
		return Mat(i.i.transpose())
		
		
class Rref(Interpretation):
	def __init__(self, i):
		self.i = i

	def __str__(self):
		return "rref " + str(self.i)
		
	def solve(self, vars):
		i = self.i.solve(vars)
		
		if type(i) is Err:
			return i
			
		if not type(i) is Mat:
			return Err("must use matrix")
			
		return Mat(i.i.rref())
		
	 
class Det(Interpretation):
	def __init__(self, i):
		self.i = i
	
	def __str__(self):
		return "det " + str(self.i)
		
	def solve(self, vars):
		i = self.i.solve(vars)
		
		if type(i) is Err:
			return i
			
		if not type(i) is Mat:
			return Err("must use matrix")
			
		return Num(i.i.determinant())
		
		
class Rank(Interpretation):
	def __init__(self, i):
		self.i = i
	
	def __str__(self):
		return "rank " + str(self.i)
		
	def solve(self, vars):
		i = self.i.solve(vars)
		
		if type(i) is Err:
			return i
			
		if not type(i) is Mat:
			return Err("must use matrix")
			
		return Num(i.i.rank())
		
		
class Invertible(Interpretation):
	def __init__(self, i):
		self.i = i
		
	def __str__(self):
		return "invertible? " + str(self.i)
		
	def solve(self, vars):
		i = self.i.solve(vars)
		
		if type(i) is Err:
			return i
			
		if not type(i) is Mat:
			return Err("must use matrix")
			
		return Bool(i.i.invertible())
		
		
class Inverse(Interpretation):
	def __init__(self, i):
		self.i = i

	def __str__(self):
		return "inverse " + str(self.i)
		
	def solve(self, vars):
		i = self.i.solve(vars)
		
		if type(i) is Err:
			return i
			
		if not type(i) is Mat:
			return Err("must use matrix")
			
		if not i.i.invertible():
			return Err("matrix is not invertible")
			
		return Mat(i.i.inverse())
		
		
class Null(Interpretation):
	def __init__(self):
		pass
	
	def __str__(self):
		return ""
		
	def solve(self, vars):
		return self
		
		
class Tuple(Interpretation):
	def __init__(self, i1, i2):
		self.i1 = i1
		self.i2 = i2

	def __str__(self):
		return str(self.i1) + ", " + str(self.i2)
		
	def size(self):
		return Tuple.__size(self)
			
	def __getitem__(self, index):
		if index < 0 or index >= self.size():
			return Err("out of bounds")
			
		if index < Tuple.__size(self.i1):
			if not type(self.i1) is Tuple:
				return self.i1
				
			return self.i1[index]
			
		else:
			index -= Tuple.__size(self.i1)
			if not type(self.i2) is Tuple:
				return self.i2
				
			return self.i2[index]
			
	def __eq__(self, t):
		if not type(t) is Tuple:
			return Bool(False)
			
		return Bool((self.i1 == t.i1).i and (self.i2 == t.i2).i)
			
	def __neg__(self):
		if type(self.i1) is Tuple:
			self.i1 = -self.i1
		if type(self.i2) is Tuple:
			self.i2 = -self.i2
			
		return Tuple(self.i2, self.i1)
		
	def solve(self, vars):
		i1 = self.i1.solve(vars)
		i2 = self.i2.solve(vars)
		
		if type(i1) is Err:
			return i1
		if type(i2) is Err:
			return i2
		
		if not type(i1) in [Mat, Num, Bool, Range, Tuple] or not type(i2) in [Mat, Num, Bool, Range, Tuple]:
			return Err("invalid tuple element")
			
		return Tuple(i1, i2)
		
		
	def __size(i):
		if not type(i) is Tuple:
			return 1
			
		return Tuple.__size(i.i1) + Tuple.__size(i.i2)
		
		
class Range(Interpretation):
	def __init__(self, i1, i2):
		self.i1 = i1
		self.i2 = i2
	
	def __str__(self):
		return str(self.i1) + ":" + str(self.i2)
		
	def size(self):
		return abs(Precision.num(self.i1) - Precision.num(self.i2))
		
	def __eq__(self, r):
		if not type(r) is Range:
			return Bool(False)
			
		return Bool(self.i1 == r.i1 and self.i2 == r.i2)
		
	def __lt__(self, r):
		if not type(r) is Range:
			return Err("invalid comparison")
			
		return Bool(self.size() < r.size())
		
	def __gt__(self, r):
		if not type(r) is Range:
			return Err("invalid comparison")
			
		return Bool(self.size() > r.size())
		
	def __le__(self, r):
		if not type(r) is Range:
			return Err("invalid comparison")
			
		return Bool(self.size() <= r.size())
		
	def __ge__(self, r):
		if not type(r) is Range:
			return Err("invalid comparison")
			
		return Bool(self.size() >= r.size())
		
	def __neg__(self):
		return Range(self.i2, self.i1)
		
	def list(self):
		if self.i1 < self.i2:
			return list(range(Precision.num(self.i1.i), Precision.num(self.i2.i) + 1))
		else:
			return (list(range(Precision.num(self.i2.i), Precision.num(self.i1.i) + 1)))[::-1]
		
	def solve(self, vars):
		i1 = self.i1.solve(vars)
		i2 = self.i2.solve(vars)
		
		if type(i1) is Err:
			return i1
		if type(i2) is Err:
			return i2
		
		if is_integer(i1) and is_integer(i2):
			return Range(i1, i2)
		elif type(i1) is Mat and type(i2) is Mat:
			return Mat(i1.i.augment(i2.i))
		else:
			return Err("range must consist of integers")
		
		
class Identity(Interpretation):
	def __init__(self, form, i):
		self.form = form
		self.i = i
	
	def __str__(self):
		if self.form == 0:
			return "zero vector " + str(self.i)
			
		return "identity " + str(self.i)
		
	def solve(self, vars):
		i = self.i.solve(vars)
		
		if type(i) is Err:
			return i
			
		if not is_integer(i):
			return Err("must use integer")
			
		if self.form == 0:
			return Mat(Matrix.Zero_Vector(Precision.num(i.i)))
		else:
			return Mat(Matrix.Identity(Precision.num(i.i)))
		
		
class Type(Interpretation):
	def __init__(self, i):
		self.i = i
	
	def __str__(self):
		if type(self.i) is Mat:
			return "matrix"
		elif type(self.i) is Num:
			return "number"
		elif type(self.i) is Bool:
			return "boolean"
		elif type(self.i) is Range:
			return "range"
		elif type(self.i) is Tuple:
			return "tuple"
		else:
			return "void"			
			
	def solve(self, vars):
		i = self.i.solve(vars)
		
		if type(i) is Err:
			return i
			
		return Type(i)
		
		
class List(Interpretation):
	def __init__(self):
		pass

	def __str__(self):
		return "list"
		
	def solve(self, vars):
		return self
		
		
class Logic(Interpretation):
	def __init__(self, operator, i1, i2=Null()):
		self.operator = operator
		self.i1 = i1
		self.i2 = i2
	
	def __str__(self):
		if self.operator == "not":
			return "not " + str(self.i1)
			
		return str(self.i1) + " " + self.operator + " " + str(self.i2)
		
	def solve(self, vars):
		i1 = self.i1.solve(vars)
		i2 = self.i2.solve(vars)
		
		if type(i1) is Err:
			return i1
		if type(i2) is Err:
			return i2
			
		if not type(i1) is Bool:
			return Err("invalid boolean")
			
		if self.operator == "not":
			return Bool(not i1.i)
			
			
		if not type(i2) is Bool:
				return Err("invalid boolean")				
		if self.operator == "and":
			return Bool(i1.i and i2.i)
		elif self.operator == "or":
			return Bool(i1.i or i2.i)
	
	
class Factorial(Interpretation):
	def __init__(self, i):
		self.i = i
	
	def __str__(self):
		return str(self.i) + "!"
		
	def solve(self, vars):
		i = self.i.solve(vars)
		
		if type(i) is Err:
			return i
			
		if not is_integer(i) or Precision.num(i) < 0:
			return Err("must use non-negative integer")
			
		base = Precision.num(i)
		factorial = 1
		for f in range(2, base + 1):
			factorial *= f
		
		return Num(factorial)
		
		
class Negate(Interpretation):
	def __init__(self, i):
		self.i = i
		
	def __str__(self):
		return "-" + str(self.i)
		
	def solve(self, vars):
		i = self.i.solve(vars)
		
		if type(i) is Err:
			return i
			
		if not type(i) in [Mat, Num, Range, Tuple]:
			return err("invalid negation")
			
		return -i
		
		
		
def is_integer(i):
	return type(i) is Num and type(Precision.num(i)) is int
	
def valid_name(name):
	invalids = ["set", "get", "rows", "row", "cols", "col", "size", "inverse",
				"transpose", "rref", "det", "rank", "true", "false", "exit", 
				"and", "or", "not", "to", "from", "type", "list", "identity"]
			
	return not name in invalids and not name.isdigit() and name.replace("_", "").isalnum()
		