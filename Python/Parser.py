from Interpretation import *

class Parser():
	def parse(s, vars):
		if s is None or vars is None or s == "" or s.isspace():
			return Null()
			
		s = s.replace("(", " (").replace(")", ") ").strip()
		
		parens = Parser.__indexes(s, '(', ')')
		blocks = Parser.__indexes(s, '[', ']')
		for index in (parens + blocks):
			if index < 0:
				return Err("unbalanced parentheses or matrix blocks")
				
		if parens[len(parens) - 1] != 0 or blocks[len(blocks) - 1] != 0:
			return Err("unbalanced parentheses or matrix blocks")
			
		
		if s.startswith("type "):
			return Type(Parser.parse(s[5:], vars))
			
		if s == "list":
			return List()
			
		try:
			return Num(float(s))
		except:
			pass
			
		index = Parser.__index_of(s, "=", parens, blocks, 0, len(s) - 1, -1, vars)
		if index >= 0 and not s[index - 1] in ["<", ">", "!", "="]:
			arr = [s[:index].strip(), s[(index + 1):]]
			if arr[0] == "":
				return Err("invalid declaration")
				
			return Declare(arr[0], Parser.parse(arr[1], vars))
			
			
		priority = Parser.__first_priority(s, [" and ", " or "], parens, blocks, 0, len(s) - 1, -1, vars)
		if not priority is None:
			arr = Parser.__split(s, priority, parens, blocks, -1, vars, 2)
			return Logic(priority.strip(), Parser.parse(arr[0], vars), Parser.parse(arr[1], vars))
			
		if s.startswith("not "):
			return Logic("not", Parser.parse(s[4:], vars))
			
			
		priority = Parser.__first_priority(s, ["==", "!=", "<=", ">=", "<", ">"], parens, blocks, 0, len(s) - 1, -1, vars)
		if not priority is None:
			arr = Parser.__split(s, priority, parens, blocks, -1, vars, 2)
			return Compare(priority, Parser.parse(arr[0], vars), Parser.parse(arr[1], vars))
			
			
		if s.startswith("get "):
			index = Parser.__index_of(s, " from ", parens, blocks, 0, len(s) - 1, 1, vars)
			if index < 0:
				return Err("invalid request")
				
			arr = Parser.__split(s, " from ", parens, blocks, 1, vars, 2)
			return Get(Parser.parse(arr[0][4:], vars), Parser.parse(arr[1], vars))
			
			
		if s.startswith("size "):
			return Size(Parser.parse(s[5:], vars))
			
			
		index = Parser.__index_of(s, ",", parens, blocks, 0, len(s) - 1, 1, vars)
		if index >= 0:
			return Tuple(Parser.parse(s[:index], vars), Parser.parse(s[(index + 1):], vars))
			
			
		if s.startswith("set "):
			arr = Parser.__split(s, " from ", parens, blocks, 1, vars, 2)
			arr = [arr[0]] + Parser.__split(arr[1], " to ", Parser.__indexes(arr[1], '(', ')'), Parser.__indexes(arr[1], '[', ']'), 1, vars, 2)
			return Set(Parser.parse(arr[0], vars), Parser.parse(arr[1], vars), Parser.parse(arr[2], vars))
			
		if s.startswith("row ") or s.startswith("rows "):
			return Rows(Parser.parse(s[4:], vars))
			
		if s.startswith("col ") or s.startswith("cols "):
			return Cols(Parser.parse(s[4:], vars))
			
		if s.startswith("square? "):
			return Square(Parser.parse(s[8:], vars))
			
		if s.startswith("symmetrical? "):
			return Symmetrical(Parser.parse(s[13:], vars))
			
		if s.startswith("invertible? "):
			return Invertible(Parser.parse(s[12:], vars))
			
		if s.startswith("inverse "):
			return Inverse(Parser.parse(s[8:], vars))
			
		if s.startswith("transpose "):
			return Transpose(Parser.parse(s[10:], vars))
			
		if s.startswith("rref "):
			return Rref(Parser.parse(s[5:], vars))
			
		if s.startswith("det "):
			return Det(Parser.parse(s[4:], vars))
			
		if s.startswith("rank "):
			return Rank(Parser.parse(s[5:], vars))
			
		if s.startswith("identity "):
			return Identity(1, Parser.parse(s[9:], vars))
			
		if s.startswith("zero vector "):
			return Identity(0, Parser.parse(s[12:], vars))
			
			
		index = Parser.__index_of(s, ":", parens, blocks, 0, len(s) - 1, 1, vars)
		if index >= 0:
			return Range(Parser.parse(s[:index], vars), Parser.parse(s[(index + 1):], vars))
			
			
		priority = Parser.__first_priority(s, ["+", "-"], parens, blocks, 0, len(s) - 1, -1, vars)
		if not priority is None:
			arr = Parser.__split(s, priority, parens, blocks, -1, vars, 2)
			if priority == "+":
				return Add(Parser.parse(arr[0], vars), Parser.parse(arr[1], vars));
			else:
				return Sub(Parser.parse(arr[0], vars), Parser.parse(arr[1], vars)); 
		
		priority = Parser.__first_priority(s, ["*", "/"], parens, blocks, 0, len(s) - 1, -1, vars)
		if not priority is None:
			arr = Parser.__split(s, priority, parens, blocks, -1, vars, 2)
			if priority == "*":
				return Mult(Parser.parse(arr[0], vars), Parser.parse(arr[1], vars));
			else:
				return Div(Parser.parse(arr[0], vars), Parser.parse(arr[1], vars)); 
				
		if s.startswith("-"):
			return Negate(Parser.parse(s[1:], vars))
			
		index = Parser.__index_of(s, "^", parens, blocks, 0, len(s) - 1, 1, vars)
		if index >= 0:
			return Pow(Parser.parse(s[:index], vars), Parser.parse(s[(index + 1):], vars))
			
		
		if s.endswith("!"):
			return Factorial(Parser.parse(s[:(len(s) - 1)], vars))
			
		
		if s == "true":
			return Bool(True)
		
		if s == "false":
			return Bool(False)
			
			
		if s.startswith("(") and s.endswith(")"):
			return Parser.parse(s[1:(len(s) - 1)], vars)
			
		if s.startswith("[") and s.endswith("]"):
			s = Parser.__parse_matrix(s[1:(len(s) - 1)], vars)
			if s.startswith("error: "):
				return Err(s[7:])
				
			try:
				m = Matrix(s)
				return Mat(m)
			except:
				return Err("invalid matrix")
				
		
		if s.count(" ") == 0:
			return Var(s)
			
		return Err("invalid command")
		
		
			
	def __parse_matrix(s, vars):
		s = s.strip()
		if s == "":
			return "error: empty matrix"
			
		s = s.replace(" (", "(")
		m = ""
		parens = Parser.__indexes(s, '(', ')')
		blocks = Parser.__indexes(s, '[', ']')
		
		arr1 = Parser.__split(s, ";", parens, blocks, 1, vars, -1)
		for row in arr1:
			arr2 = Parser.__split(row, " ", Parser.__indexes(row, '(', ')'), Parser.__indexes(row, '[', ']'), 1, vars, -1)
			for value in arr2:
				val = Parser.parse(value, vars).solve(vars)
				
				if type(val) is Err:
					return "error: " + val.message
					
				if not type(val) in [Num, Range]:
					return "error: must use range or integers"
					
				if type(val) is Num:
					m += str(val) + " "
				else:
					for v in val.list():
						m += str(v) + " "
						
			m += "; "
			
		return m[:(len(m) -2)]
		
		
	def __indexes(s, front, back):
		parens = []
		paren = 0
		for c in s:
			if c == front:
				paren += 1
			elif c == back:
				paren -= 1
				
			parens.append(paren)
			
		return parens
			
		
	def __index_of(s, c, parens, blocks, start, end, direction, vars):
		if direction > 0:
			index = s.find(c, start, end)
			start = index + len(c)
		else:
			index = s.rfind(c, start, end)
			end = index - len(c)
		
		if index < 0:
			return -1
			
		if parens[index] == 0 and blocks[index] == 0:
			
			if c == "-":
				i = Parser.parse(s[0:index], vars).solve(vars)
				if type(i) in [Mat, Num]:
					return index
					
			else:
				return index
				
		return Parser.__index_of(s, c, parens, blocks, start, end, direction, vars)
		
	def __split(s, c, parens, blocks, direction, vars, limit=-1):			
		index = Parser.__index_of(s, c, parens, blocks, 0, len(s) - 1, direction, vars)
		if index < 0:
			return [s.strip()]
			
		first = s[:index].strip()
		second = s[index + len(c):].strip()
		
		if limit > 2 or limit < 0:
			parens1 = Parser.__indexes(first, '(', ')')
			parens2 = Parser.__indexes(second, '(', ')')
			blocks1 = Parser.__indexes(first, '[', ']')
			blocks2 = Parser.__indexes(second, '[', ']')
			
			if direction > 0:
				if Parser.__index_of(second, c, parens2, blocks2, 0, len(second) - 1, direction, vars) == -1:
					return [first, second]
				else:
					split = Parser.__split(second, c, parens2, blocks2, direction, vars, limit - 1)
					return [first] + split
					
			else:
				start = len(first) - 1
				if Parser.__index_of(first, c, parens1, blocks1, 0, len(first) - 1, direction, vars) == -1:
					return [first, second]
				else:
					split = Parser.__split(first, c, parens1, blocks1, direction, vars, limit - 1)
					return split + [second]
					
		return [first, second]
		
	def __first_priority(s, arr, parens, blocks, start, end, direction, vars):
		priority = arr[0]
		index = Parser.__index_of(s, priority, parens, blocks, start, end, direction, vars)
		
		for tok in arr:
			index2 = Parser.__index_of(s, tok, parens, blocks, start, end, direction, vars)			
			if index2 > index:
				priority = tok
				index = Parser.__index_of(s, priority, parens, blocks, start, end, direction, vars)
		
		if (index < 0):
			return None
		
		return priority
		