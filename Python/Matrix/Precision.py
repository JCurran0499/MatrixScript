from decimal import *

getcontext().rounding = ROUND_HALF_UP

class Precision():
	__max_prec = 0
		
	def precision(num):
		precision = 0
		while not -1 < num < 1:
			precision += 1
			num /= 10
			
		return precision
		
	def decimal(value, prec):
		try:
			value = float(value)
		except:
			return None
			
		precision = Precision.precision(value)
		
		if precision > Precision.__max_prec:
			Precision.__max_prec = precision * 2
			
		getcontext().prec = precision + prec
		d = Decimal(value) * 1
		if abs(d) < Decimal(1 / (10 ** prec)):
			d = Decimal(0)
		getcontext().prec = Precision.__max_prec + prec
		
		return d
		
	def num(value):			
		try:
			if str(float(value)).endswith(".0"):
				return int(value)
			else:
				return float(value)
		except:
			return None
