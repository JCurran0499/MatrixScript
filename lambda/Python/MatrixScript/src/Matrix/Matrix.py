import numpy as np
import re
from decimal import Decimal, getcontext


class Matrix:
    @staticmethod
    def set_precision(prec):
        getcontext().prec = prec

    def __init__(self, shape: tuple = None, arr: list = None, string: str = None):
        if shape is not None:
            if len(shape) != 2 or shape[0] <= 0 or shape[1] <= 0:
                raise IndexError("Invalid Dimensions")

            self._matrix = np.full(shape=shape, fill_value=Decimal(0), dtype=Decimal)

        elif arr is not None:
            try:
                new_arr = np.array(arr, dtype=float)
            except ValueError:
                raise ValueError("Matrix Requires Number Values")

            if new_arr.ndim != 2:
                raise IndexError("Invalid Dimensions")

            new_arr = [[Decimal(d) for d in row] for row in new_arr]
            self._matrix = np.array(new_arr, dtype=Decimal)

        elif string is not None:
            try:
                self._matrix = np.matrix(string).A
            except ValueError:
                raise ValueError("Matrix Requires Number Values")

        else:
            raise ValueError("Constructor Cannot Be Empty")

    @staticmethod
    def identity(i):
        return Matrix(np.identity(n=i))

    @staticmethod
    def zero_vector(i):
        return Matrix(np.zeros(i))

    def rows(self):
        return self._matrix.shape[0]

    def cols(self):
        return self._matrix.shape[1]

    def size(self):
        return self._matrix.size

    def is_square(self):
        return self.rows() == self.cols()

    def __getitem__(self, key):
        if type(key) is int:
            return Matrix(self._matrix[key])
        elif type(key) is tuple:
            return self._matrix[key]

    def __setitem__(self, key, value):
        self._matrix[key] = value


m = Matrix(string="1 2 3 ; 4 5 6")
print(m[0])
