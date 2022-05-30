from .Matrix import *
import unittest

class Tests(unittest.TestCase):

	def test01(self):
		self.assertEqual(True, True)
		
	def test02(self):
		self.assertTrue(True)
		
	def test03(self):
		self.assertFalse(False)
		
		
if __name__ == "__main__":
	unittest.main()