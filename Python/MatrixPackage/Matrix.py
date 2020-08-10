from .Precision import *

class MatrixError(Exception):
	def __init__(self, message):
		self.message = message
		

class Matrix():
	prec = 5
	def __init__(self, x, y=None):
		self.__matrix = []
		if type(x) is int and type(x) is int:
			if x <= 0 or y <= 0:
				raise MatrixError("invalid dimensions")
				
			for i in range(0, x):
				self.__matrix.append([])
				for j in range(0, y):
					self.__matrix[i].append(Precision.decimal(0, Matrix.prec))
					
		elif type(x) is list and y is None:
		
			for i in range(0, len(x)):
				if not type(x[i]) is list:
					raise MatrixError("improper form")
					
				self.__matrix.append([Precision.decimal(val, Matrix.prec) for val in x[i]])
				
			if not Matrix.__square_arr(self.__matrix):
				raise MatrixError("invalid dimensions")
				
		elif type(x) is Matrix and y is None:
			self.__matrix = [[val for val in row] for row in x.__matrix]
			
		elif type(x) is str and y is None:
			rows = x.split(";")
			for i in range(0, len(rows)):
				self.__matrix.append([])
				values = rows[i].split()
				
				for j in range(0, len(values)):
					self.__matrix[i].append(Precision.decimal(values[j], Matrix.prec))
					
			if not Matrix.__square_arr(self.__matrix):
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
				string += str(Precision.num(self.__matrix[i][j])) + "  "
				
			if i < len(self.__matrix) - 1:
				string += ";  "
				
		return string + "]"
		
	def print(self):
		widths = self.__widths()
		
		for r in range(0, self.rows()):
			print("[ ", end="")
			for c in range(0, self.cols()):
				val = str(Precision.num(self.__matrix[r][c]))
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
		if not (type(r) is int and type(c) is int) or r < 0 or c < 0 or r >= self.rows() or c >= self.cols():
			return None
			
		return Precision.num(self.__matrix[r][c])
		
	def get_column(self, c):
		if not type(c) is int or c < 0 or c >= self.cols():
			return None
			
		m = Matrix(self.rows(), 1)
		for i in range(0, self.rows()):
			m.__matrix[i][0] = self.__matrix[i][c]
			
		return m
		
	def get_row(self, r):
		if not type(r) is int or r < 0 or r >= self.rows():
			return None
		
		return Matrix([self.__matrix[r]])
		
	def set_value(self, r, c, val):
		if not (type(r) is int and type(c) is int):
			raise MatrixError("must use integers")
		if r < 0 or c < 0 or r >= self.rows() or c >= self.cols():
			raise MatrixError("out of bounds")
		if not type(val) in [int, float, Decimal]:
			raise MatrixError("invalid value type")
			
		self.__matrix[r][c] = Precision.decimal(val, Matrix.prec)
		
		
	def set_row(self, r, val):
		if not type(r) is int:
			raise MatrixError("must use integer")
		if r < 0 or r >= self.rows():
			raise MatrixError("out of bounds")
		if not type(val) is Matrix:
			raise MatrixError("invalid value type")
		if val.rows() != 1 or val.cols() != self.cols():
			raise MatrixError("invalid dimensions")
			
		self.__matrix[r] = [x for x in val.__matrix[0]]
		
	def set_column(self, c, val):
		if not type(c) is int:
			raise MatrixError("must use integer")
		if c < 0 or c >= self.cols():
			raise MatrixError("out of bounds")
		if not type(val) is Matrix:
			raise MatrixError("invalid value type")
		if val.cols() != 1 or val.rows() != self.rows():
			raise MatrixError("invalid dimensions")
			
		
		for i in range(0, self.rows()):
			self.__matrix[i][c] = val.__matrix[i][0]
			
	def __add__(self, m):
		if not type(m) is Matrix or self.rows() != m.rows() or self.cols() != m.cols():
			return None
			
		new_matrix = Matrix(self.rows(), self.cols())
		for i in range(0, self.rows()):
			for j in range(0, self.cols()):
				new_matrix.__matrix[i][j] = Precision.decimal(self.__matrix[i][j] + m.__matrix[i][j], Matrix.prec)
				
		return new_matrix
		
	def __sub__(self, m):
		if not type(m) is Matrix or self.rows() != m.rows() or self.cols() != m.cols():
			return None
			
		new_matrix = Matrix(self.rows(), self.cols())
		for i in range(0, self.rows()):
			for j in range(0, self.cols()):
				new_matrix.__matrix[i][j] = Precision.decimal(self.__matrix[i][j] - m.__matrix[i][j], Matrix.prec)
				
		return new_matrix
		
	def __mul__(self, x):
		if type(x) in [int, float, Decimal]:
			new_matrix = Matrix(self.rows(), self.cols())
			for i in range(0, self.rows()):
				for j in range(0, self.cols()):
					new_matrix.__matrix[i][j] = Precision.decimal(self.__matrix[i][j] * Precision.decimal(x), Matrix.prec)
					
			return new_matrix
		
		if type(x) is Matrix:
			if x.rows() != self.cols():
				raise MatrixError("invalid dimensions")
				
			new_matrix = Matrix(self.rows(), x.cols())
			for i in range(0, self.rows()):
				for j in range(0, x.cols()):
					sum = Precision.decimal(0, Matrix.prec)
					for k in range(x.rows()):
						sum = Precision.decimal(sum + Precision.decimal(self.__matrix[i][k] * x.__matrix[k][j], Matrix.prec), Matrix.prec)
						
					new_matrix.__matrix[i][j] = sum
					
			return new_matrix
		
		return None
		
	def __truediv__(self, d):
		if not type(d) in [int, float, Decimal] or Precision.num(d) == 0:
			return None
			
		new_matrix = Matrix(self.rows(), self.cols())
		for i in range(0, self.rows()):
			for j in range(0, self.cols()):
				new_matrix.__matrix[i][j] = Precision.decimal(self.__matrix[i][j] / Precision.decimal(d, Matrix.prec), Matrix.prec)
				
		return new_matrix
		
	def __neg__(self):
		return self * -1
		
	def __pow__(self, p):
		if not self.is_square() or Precision.num(p) < 0:
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
		determinant = Precision.decimal(1, Matrix.prec)
		
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
						scale = Precision.decimal(m.__matrix[index_row][i] / m.__matrix[index_row][index_col] * -1, Matrix.prec)
						m.set_column(i, (m.get_column(index_col) * scale) + m.get_column(i))
						
				if m.get_value(index_row, index_col) != 1:
					scale = Precision.decimal(1 / m.__matrix[index_row][index_col], Matrix.prec)
					m.set_column(index_col, m.get_column(index_col) * scale)
					determinant = Precision.decimal(determinant / scale, Matrix.prec)
					
				index_col += 1
				
			index_row += 1
			
		if not m == Matrix.Identity(rows):
			determinant = Precision.decimal(0, Matrix.prec)
			
		return [m.transpose(), Precision.num(determinant)]
	
	def __zero_from_point(arr, point):
		return arr == [arr[i] for i in range(0, point)] + [0 for i in range(point, len(arr))]			
		
	def __square_arr(arr):
		if len(arr) == 0:
			return True
			
		length = len(arr[0])
		for i in range(1, len(arr)):
			if not len(arr[i]) == length:
				return False
				
		return True
			
	def __widths(self):
		widths = []
		for c in range(0, self.cols()):
			max = 0
			for r in range(0, self.rows()):
				length = len(str(Precision.num(self.__matrix[r][c])))
				if length > max:
					max = length
					
			widths.append(max)
			
		return widths
		