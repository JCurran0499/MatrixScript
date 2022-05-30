'''
John Curran

This class simulates a matrix of numbers. A matrix is simulated 
with a two dimensional array of BigDecimal values. BigDecimal is
used in order to ensure precision when it comes to mathematical 
calculations. The methods below define the behavior of the matrix. 
In this case, each row in a matrix is an array of BigDecimal values 
in the two dimensional array, so each value in the matrix can be 
retrived from the array using matrix[row][column]. All methods 
that return objects will return null on an error, so it is the 
responsibility of the user to check for that if they think there
might be an error. Methods that do not return values, along with
constructors, will throw an exception upon an error
'''

from .Precision import *

class MatrixError(Exception):
	def __init__(self, message):
		self.message = message
		

class Matrix():
	__prec = 10
	__print_prec = 5
	def setprecision(p):
		p = Precision.num(p)
		if type(p) is int:
			Matrix.__prec = p
			
	def setprintprecision(p):
		p = Precision.num(p)
		if type(p) is int:
			Matrix.__print_prec = p
			
		
	def __init__(self, x, y=None):
		self.__matrix = []
		if type(Precision.num(x)) is int and type(Precision.num(y)) is int:
			x = Precision.num(x)
			y = Precision.num(y)
			if x <= 0 or y <= 0:
				raise MatrixError("invalid dimensions")
				
			for i in range(0, x):
				self.__matrix.append([])
				for j in range(0, y):
					self.__matrix[i].append(Precision.decimal(0, Matrix.__prec))
					
		elif type(x) is list and y is None:
		
			for i in range(0, len(x)):
				if not type(x[i]) is list:
					raise MatrixError("improper form")
					
				self.__matrix.append([Precision.decimal(val, Matrix.__prec) for val in x[i]])
				
			if not Matrix.__even_arr(self.__matrix):
				raise MatrixError("invalid dimensions")
				
		elif type(x) is Matrix and y is None:
			self.__matrix = [[val for val in row] for row in x.__matrix]
			
		elif type(x) is str and y is None:			
			rows = x.split(";")
			for i in range(0, len(rows)):
				self.__matrix.append([])
				values = rows[i].split()
				for j in range(0, len(values)):
					self.__matrix[i].append(Precision.decimal(values[j], Matrix.__prec))
					
			if not Matrix.__even_arr(self.__matrix):
				raise MatrixError("invalid dimensions")
			
		else:
			raise MatrixError("invalid argument")
			
			
			
	def Identity(size):
		m = Matrix(size, size)
		for i in range(0, size):
				m.set_value(i, i, 1)
				
		return m
		
	def Zero_Vector(length):
		return Matrix(length, 1)
			
			
	def __str__(self):
		string = "[  "
		for i in range(0, len(self.__matrix)):
			for j in range(0, len(self.__matrix[0])):
				string += str(Precision.num(Precision.decimal(self.__matrix[i][j], Matrix.__print_prec))) + "  "
				
			if i < len(self.__matrix) - 1:
				string += ";  "
				
		return string + "]"
		
	def print(self):
		widths = self.__widths()
		
		for r in range(0, self.rows()):
			print("[ ", end="")
			for c in range(0, self.cols()):
				val = str(Precision.num(Precision.decimal(self.__matrix[r][c], Matrix.__print_prec)))
				for space in range(len(val), widths[c]):
					print(" ", end="")
			
				print(val, end="")
				if c < self.cols() - 1:
					print("  ", end="")
			
			print(" ]")
			
		print("")
			
	def rows(self):
		return len(self.__matrix)
		
	def cols(self):
		return len(self.__matrix[0])
		
	def size(self):
		return self.rows() * self.cols()
		
	def is_square(self):
		return self.rows() == self.cols()
		
	def is_symmetrical(self):
		return self == self.transpose()
		
	def get_value(self, r, c):
		r, c = Precision.num(r), Precision.num(c)
		if not (type(r) is int and type(c) is int) or r < 0 or c < 0 or r >= self.rows() or c >= self.cols():
			return None
			
		return Precision.num(self.__matrix[r][c])			
		
	def get_row(self, r):
		if type(Precision.num(r)) is int:
			r = Precision.num(r)
			r = (r,r + 1)			
		
		if not (type(r) is tuple and len(r) == 2):
			return None 
			
		r1, r2 = r
		r1, r2 = Precision.num(r1), Precision.num(r2)
		if not (type(r1) is int and type(r2) is int) or r1 < 0 or r1 >= self.rows() or r2 > self.rows() or r2 <= r1:
			return None
		
		m = []
		for row in range(r1, r2):
			m.append([val for val in self.__matrix[row]])
			
		return Matrix(m)
		
	def get_column(self, c):
		m = self.transpose().get_row(c)
		if m is None:
			return None
			
		return m.transpose()
		
	def set_value(self, r, c, val):
		r, c = Precision.num(r), Precision.num(c)
		val = Precision.decimal(val, Matrix.__prec)
		if not (type(r) is int and type(c) is int):
			raise MatrixError("must use integers")
		if r < 0 or c < 0 or r >= self.rows() or c >= self.cols():
			raise MatrixError("out of bounds")
		if not type(val) is Decimal:
			raise MatrixError("invalid value type")
			
		self.__matrix[r][c] = val		
		
	def set_row(self, r, val):
		if type(Precision.num(r)) is int:
			r = Precision.num(r)
			r = (r, r + 1)
			
		if not (type(r) is tuple and len(r) == 2):
			raise MatrixError("invalid argument")
			
		r1, r2 = r
		r1, r2 = Precision.num(r1), Precision.num(r2)
		if not (type(r1) is int and type(r2) is int):
			raise MatrixError("invalid argument")
		if r1 < 0 or r1 >= self.rows() or r2 > self.rows() or r2 <= r1:
			raise MatrixError("out of bounds")
		if not type(val) is Matrix:
			raise MatrixError("invalid value type")
		if not (val.rows() == r2 - r1 and val.cols() == self.cols()):
			raise MatrixError("invalid dimensions")
			
		for row in range(r1, r2):
			self.__matrix[row] = [v for v in val.__matrix[row - r1]]
		
	def set_column(self, c, val):
		m = self.transpose()
		m.set_row(c, val.transpose())
		self.__matrix = m.transpose().__matrix
			
	def __add__(self, m):
		if not type(m) is Matrix or self.rows() != m.rows() or self.cols() != m.cols():
			return None
			
		new_matrix = Matrix(self.rows(), self.cols())
		for i in range(0, self.rows()):
			for j in range(0, self.cols()):
				new_matrix.__matrix[i][j] = Precision.decimal(self.__matrix[i][j] + m.__matrix[i][j], Matrix.__prec)
				
		return new_matrix
		
	def __sub__(self, m):
		if not type(m) is Matrix or self.rows() != m.rows() or self.cols() != m.cols():
			return None
			
		new_matrix = Matrix(self.rows(), self.cols())
		for i in range(0, self.rows()):
			for j in range(0, self.cols()):
				new_matrix.__matrix[i][j] = Precision.decimal(self.__matrix[i][j] - m.__matrix[i][j], Matrix.__prec)
				
		return new_matrix
		
	def __mul__(self, x):
		if type(x) in [int, float, Decimal]:
			new_matrix = Matrix(self.rows(), self.cols())
			for i in range(0, self.rows()):
				for j in range(0, self.cols()):
					new_matrix.__matrix[i][j] = Precision.decimal(self.__matrix[i][j] * Precision.decimal(x, Matrix.__prec), Matrix.__prec)
					
			return new_matrix
		
		if type(x) is Matrix:
			if x.rows() != self.cols():
				return None
				
			new_matrix = Matrix(self.rows(), x.cols())
			for i in range(0, self.rows()):
				for j in range(0, x.cols()):
					sum = Precision.decimal(0, Matrix.__prec)
					for k in range(x.rows()):
						sum = Precision.decimal(sum + Precision.decimal(self.__matrix[i][k] * x.__matrix[k][j], Matrix.__prec), Matrix.__prec)
						
					new_matrix.__matrix[i][j] = sum
					
			return new_matrix
		
		return None
		
	def __truediv__(self, d):
		if not type(d) in [int, float, Decimal] or Precision.num(d) == 0:
			return None
			
		new_matrix = Matrix(self.rows(), self.cols())
		for i in range(0, self.rows()):
			for j in range(0, self.cols()):
				new_matrix.__matrix[i][j] = Precision.decimal(self.__matrix[i][j] / Precision.decimal(d, Matrix.__prec), Matrix.__prec)
				
		return new_matrix
		
	def __neg__(self):
		return self * -1
		
	def __pow__(self, p):
		p = Precision.num(p)
		if not self.is_square() or not type(p) is int or p < 0:
			return None
			
		m = Matrix.Identity(self.rows())
		for i in range(0, p):
			m *= self
			
		return m
		
	def __eq__(self, m):
		if not type(m) is Matrix or m.rows() != self.rows() or m.cols() != self.cols():
			return False
			
		for i in range(0, m.rows()):
			for j in range(0, m.cols()):
				if float(self.__matrix[i][j]) != float(m.__matrix[i][j]):
					return False
					
		return True
		
	def transpose(self):
		transpose = Matrix(self.cols(), self.rows())
		for i in range(0, self.rows()):
			for j in range(0, self.cols()):
				transpose.__matrix[j][i] = self.__matrix[i][j]
				
		return transpose
		
	def list(self):
		return list(map(lambda row: [Precision.num(val) for val in row], self.__matrix))
		
	def augment(self, m):
		if not type(m) is Matrix or m.rows() != self.rows():
			return None
			
		new_matrix = Matrix(self)
		for i in range(0, self.rows()):
			new_matrix.__matrix[i] += m.__matrix[i]
			
		return new_matrix
		
	def rref(self):
		return self.__rref_det()[0]
		
	def determinant(self):
		if not self.is_square():
			return 0
			
		return self.__rref_det()[1]
		
	def invertible(self):
		return self.determinant() != 0
		
	def inverse(self):
		if not self.invertible():
			return None
			
		m = self.augment(Matrix.Identity(self.cols())).rref()
		new_matrix = m.get_column(self.cols())
		for i in range(self.cols() + 1, m.cols()):
			new_matrix = new_matrix.augment(m.get_column(i))
			
		return new_matrix
		
	def rank(self):
		r = self.rref()
		rank = 0
		for i in range(0, r.rows()):
			if not Matrix.__zero_from_point(r.list()[i], 0):
				rank += 1
				
		return rank
		
		
	# private helper methods
	
	def __rref_det(self):
		m = self.transpose()
		index_row, index_col, rows, cols = 0, 0, m.rows(), m.cols()
		determinant = Precision.decimal(1, Matrix.__prec)
		
		while index_row < rows and index_col < cols:
			if not Matrix.__zero_from_point(m.list()[index_row], index_col):
				if m.get_value(index_row, index_col) == 0:
					j = index_col + 1
					
					while m.get_value(index_row, j) == 0:
						j += 1
					v = m.get_column(j)
					m.set_column(j, m.get_column(index_col))
					m.set_column(index_col, v)
					determinant *= -1
					
				for i in range(0, cols):
					if i != index_col and m.get_value(index_row, i) != 0:
						scale = Precision.decimal(m.__matrix[index_row][i] / m.__matrix[index_row][index_col] * -1, Matrix.__prec)
						m.set_column(i, (m.get_column(index_col) * scale) + m.get_column(i))
						
				if m.get_value(index_row, index_col) != 1:
					scale = Precision.decimal(1 / m.__matrix[index_row][index_col], Matrix.__prec)
					m.set_column(index_col, m.get_column(index_col) * scale)
					determinant = Precision.decimal(determinant / scale, Matrix.__prec)
					
				index_col += 1
				
			index_row += 1
			
		if not m == Matrix.Identity(rows):
			determinant = Precision.decimal(0, Matrix.__prec)
			
		return [m.transpose(), Precision.num(determinant)]
	
	def __zero_from_point(arr, point):
		return arr == [arr[i] for i in range(0, point)] + [0 for i in range(point, len(arr))]			
		
	def __even_arr(arr):
		if len(arr) == 0:
			return False
			
		for i in range(0, len(arr)):
			if not len(arr[i]) == len(arr[0]) or len(arr[i]) == 0:
				return False
				
		return True
			
	def __widths(self):
		widths = []
		for c in range(0, self.cols()):
			max = 0
			for r in range(0, self.rows()):
				length = len(str(Precision.num(Precision.decimal(self.__matrix[r][c], Matrix.__print_prec))))
				if length > max:
					max = length
					
			widths.append(max)
			
		return widths
		