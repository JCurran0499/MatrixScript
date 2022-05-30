/*
 * John Curran
 * 
 * This is a collection of JUnit Tests, designed to 
 * test the accuracy of the Matrix class methods
 */

package Tests;

import static org.junit.Assert.fail;
import org.junit.jupiter.api.Test;

import Matrix.Matrix;

class MatrixTests {
	
	//tests matrix constructors
	@Test public void test00() {
		Matrix m1 = new Matrix(2, 3);
		assert(m1.rows() == 2);
		assert(m1.cols() == 3);
		assert(m1.size() == 6);
		System.out.println("1.a");
		m1.print();
		
		double[][] m = new double[3][2];
		m[0][0] = 1; m[0][1] = 5;
		m[1][0] = 4; m[1][1] = 6;
		m[2][0] = 0; m[2][1] = 9;
		Matrix m2 = new Matrix(m);
		System.out.println("1.b");
		m2.print();
		
		Matrix m3 = new Matrix(m1);
		assert(m3.rows() == 2);
		assert(m3.cols() == 3);
		assert(m3.size() == 6);
		assert(m3.equals(m1));
		
		Matrix m4 = new Matrix("5 6 7 7; 8 4 5 9; 7 6 1 -50");
		System.out.println("1.c");
		m4.print();
		assert(m4.rows() == 3);
		assert(m4.cols() == 4);
		assert(m4.size() == 12);
		
		Matrix m5 = new Matrix(m4);
		assert(m5.rows() == 3);
		assert(m5.cols() == 4);
		assert(m5.size() == 12);
		assert(m5.equals(m4));
	}
	
	//tests printing a matrix and the identity matrix
	@Test public void test01() {
		Matrix identity4 = Matrix.Identity(4);
		System.out.println("2.a");
		identity4.print();
		System.out.println();
		
		Matrix m = new Matrix("1 2 3 4 ; 2 3 4 5 ; 3 4 5 6");
		System.out.println("2.b");
		m.print();
		System.out.println();
		
		assert(m.rows() == 3);
		assert(m.cols() == 4);
		assert(Matrix.Identity(3).equals(new Matrix("1 0 0 ; 0 1 0; 0 0 1.00")));
		assert(Matrix.Identity(1).equals(new Matrix("1")));
	}
	
	//tests converting a matrix to a String
	@Test public void test02() {
		double[] col1 = new double[] {1,12,1,1};
		double[] col2 = new double[] {2,3.56,2,2.778};
		double[] col3 = new double[] {3,3,3.0,3.0000};
		double[] col4 = new double[] {0,4.2,4,000};
		Matrix m = new Matrix(new double[][] {col1, col2, col3, col4});
		assert(m.toString().equals("[ 1 12 1 1 ; 2 3.56 2 2.778 ; 3 3 3 3 ; 0 4.2 4 0 ]"));
	}
	
	//tests the zero vector
	@Test public void test03() {
		Matrix m = Matrix.ZeroVector(5);
		assert(m.equals(new Matrix("0 ; 0; 0; 0.0; 0")));
		
		m = Matrix.ZeroVector(1);
		assert(m.equals(new Matrix(new double[][] {new double[] {0}})));
	}
	
	//tests whether a matrix is square or not
	@Test public void test04() {
		Matrix m1 = new Matrix("5 6 -7 ; 1 2 4; 0 0 -578");		
		assert(m1.isSquare());
			
		Matrix m2 = new Matrix("0");
		assert(m2.isSquare());
			
		Matrix m3 = new Matrix("0 0");
		assert(!m3.isSquare());
		
		Matrix m4 = new Matrix("0 0 ; 1 1 ; 2 2");
		assert(!m4.isSquare());
	}
	
	//tests whether a matrix is symmetrical or not
	@Test public void test05() {
		Matrix m = new Matrix("1 2 3 4 ; 2 3 4 1 ; 3 4 1 2 ; 4 1 2 3");		
		assert(m.isSymmetrical());
		
		m.setValue(0, 2, 7.99);
		assert(!m.isSymmetrical());
	}
	
	//tests getting values at specific points in the matrix
	@Test public void test06() {
		double[] arr = new double[] {1,2,3,4};
			
		Matrix m1 = new Matrix(new double[][] {arr, arr, arr, arr});
		assert(m1.getValue(1, 1) == 2);
		assert(m1.getValue(0, 0) == 1);
		assert(m1.getValue(3, 2) == 3);
		assert(m1.getValue(0, 2) == 3);
		assert(m1.getValue(1, 2) == 3);
	}
	
	//tests altering a copy matrix does not alter the original matrix
	@Test public void test07() {
		double[] col1 = new double[] {1,1,1,1};
		double[] col2 = new double[] {2,2,2,2};
		double[] col3 = new double[] {3,3,3,3};
		double[] col4 = new double[] {4,4,4,4};
			
		Matrix m1 = new Matrix(new double[][] {col1, col2, col3, col4});
		Matrix m2 = new Matrix(m1);
			
		m2.setValue(0, 0, 12);
		assert(m1.getValue(0, 0) == 1);
		assert(m2.getValue(0, 0) == 12);
	}
	
	//tests getting a column, and that altering it doesn't alter the column
	//in the matrix
	@Test public void test08() {
		Matrix m = new Matrix(3, 2);
		m.setValue(0, 0, 5);
		m.setValue(1, 0, 6);
		m.setValue(2, 0, 7);
		
		Matrix v = m.getColumn(0);
		v.setValue(0, 0, 10);
		
		assert(v.getValue(0, 0) == 10);
		assert(m.getValue(0, 0) == 5);
	}
	
	//tests setting a column in the matrix
	@Test public void test09() {
		Matrix col = new Matrix("0 ; 0 ; 0");
		double[] col1 = new double[] {1, 2, 3, 4};
		double[] col2 = new double[] {1, 2, 3, 4};
		double[] col3 = new double[] {1, 2, 3, 4};
		Matrix m = new Matrix(new double[][] {col1, col2, col3});
		
		m.setColumn(0, col);
		m.setColumn(2, col);
		System.out.println("7");
		m.print();
		System.out.println();
	}
	
	//tests getting a row, and that altering it doesn't alter the column
	//in the matrix
	@Test public void test10() {
		double[] col1 = new double[] {1, 2, 3, 4};
		double[] col2 = new double[] {5, 2, 3, 4};
		double[] col3 = new double[] {7, 2, 3, 4};
		Matrix m = new Matrix(new double[][] {col1, col2, col3});
		
		Matrix row = m.getRow(0);
		assert(row.rows() == 1);
		assert(row.cols() == 4);
		assert(row.getValue(0,1) == 2);
		assert(row.getValue(0,0) == 1);
		
		row.setValue(0, 1, 0);
		assert(row.getValue(0, 1) == 0);
		assert(m.getValue(0, 1) == 2);
	}
	
	//tests setting a row
	@Test public void test11() {
		double[] col1 = new double[] {1, 2, 3, 4};
		double[] col2 = new double[] {5, 2, 3, 4};
		double[] col3 = new double[] {7, 2, 3, 4};
		Matrix m = new Matrix(new double[][] {col1, col2, col3});
		
		m.setRow(1, new Matrix("0 0 0 0"));
		assert(m.equals(new Matrix("1 2 3 4 ; 0 0 0 0; 7 2 3 4")));
	}
	
	//tests adding two matrices together
	@Test public void test12() {
		Matrix m1 = new Matrix("1 1 1 1 ; 2 2 2 2 ; 3 3 3 3; 4 4 4 4");
		
		Matrix m2 = new Matrix("5 3 0 0 ; 7 0 9 -15 ; 7 7 4 1; 3 1 1 -1");
		
		Matrix m3 = m1.add(m2);
		System.out.println("9");
		m3.print();
		System.out.println();
	}
	
	//tests subtracting two matrices
	@Test public void test13() {
		Matrix m1 = new Matrix("1 1 1 1 ; 2 2 2 2 ; 3 3 3 3; 4 4 4 4");
		
		Matrix m2 = new Matrix("5 3 0 0 ; 7 0 9 -15 ; 7 7 4 1; 3 1 1 -1");
		
		Matrix m3 = m1.subtract(m2);
		System.out.println("10");
		m3.print();
		System.out.println();
	}
	
	//tests multiplying a matrix by a constant
	@Test public void test14() {
		Matrix m = new Matrix("5 3 ; 7 0 ; 7 7");
		
		m = m.multiply(5);
		assert(m.getColumn(0).equals(new Matrix ("25;35;35")));
		assert(m.getColumn(1).equals(new Matrix ("15;0;35")));
	}
	
	//tests multiplying a matrix and a vector
	@Test public void test15() {
		Matrix m = new Matrix("1 5 ; 2 2; 3 3; 4 4");
	
		Matrix v = new Matrix(new double[][] {new double[] {5}, new double[] {4}});
		Matrix result = new Matrix(new double[][] {new double[] {25}, new double[] {18}, new double[] {27}, new double[] {36}});
		
		assert(m.multiply(v).equals(result));
		
		v = new Matrix("0;0");
		result = new Matrix("0;0;0;0");
		assert(m.multiply(v).equals(result));
	}
	
	//tests multiplying two matrices
	@Test public void test16() {
		Matrix m1 = new Matrix("1 5;2 2; 3 3; 4 4");
		Matrix m2 = new Matrix("1 7 0 2 0 ; 2 3 0 2 2");
		Matrix result = new Matrix("11 22 0 12 10; 6 20 0 8 4; 9 30 0 12 6; 12 40 0 16 8");
		
		assert(m1.multiply(m2).equals(result));
	}
	
	//tests that multiplying by the identity matrix does not change the matrix
	@Test public void test17() {
		Matrix m = new Matrix(new double[][] {new double[] {1,2,3, 4}, new double[] {5,6,0, 75}});		
		assert(m.multiply(Matrix.Identity(4)).equals(m));
	}
	
	//tests dividing a matrix by a constant
	@Test public void test18() {
		double[] col1 = new double[] {1, 2, 3, 4};
		double[] col2 = new double[] {5, 2, 3, 4};
		double[] col3 = new double[] {7, 2, 3, 4};
		Matrix m = new Matrix(new double[][] {col1, col2, col3});
		m = m.divide(4);
		
		assert(m.equals(new Matrix("0.25 0.5 0.75 1 ; 1.25 0.5 0.75 1 ; 1.75 0.5 0.75 1")));
	}
	
	//tests bringing a matrix to a power
	@Test public void test19() {
		Matrix m = new Matrix("1 5 1 5; 2 2 2 2; 3 3 3 3; 4 4 4 4");
		Matrix result = new Matrix("364 500 364 500; 248 328 248 328; 372 492 372 492; 496 656 496 656");
		
		assert(m.toPower(3).equals(result));
	}
	
	//tests that the 0 power is the identity matrix
	@Test public void test20() {
		double[] col1 = new double[] {1, 2, 3, 4};
		double[] col2 = new double[] {5, 2, 3, 4};
		double[] col3 = new double[] {1, 2, 3, 4};
		double[] col4 = new double[] {5, 2, 3, 4};
		Matrix m = new Matrix(new double[][] {col1, col2, col3, col4});
		
		assert(m.toPower(0).equals(Matrix.Identity(4)));
	}
	
	//tests getting the transpose of a matrix
	@Test public void test21() {
		Matrix m = new Matrix("1 5; 2 2; 3 3; 4 4");
		Matrix tp = new Matrix("1 2 3 4;    5 2  3 4");
		
		m.transpose();
		assert(m.transpose().equals(tp));
		assert(tp.transpose().equals(m));
	}
	
	//tests the transpose of a 1x1 matrix and the identity matrix
	@Test public void test22() {
		Matrix m = new Matrix(new double[][] {new double[] {1}});
		assert(m.transpose().equals(m));
		assert(Matrix.Identity(3).transpose().equals(Matrix.Identity(3)));
	}
	
	//tests converting a matrix to an array
	@Test public void test23() {
		Matrix m = new Matrix("5 2.55 6 7 0 ; 1 2 5 6 8.8");
		double[][] arr = m.toArray();
		
		assert(arr.length == 2 && arr[0].length == 5);
		assert(arr[0][0] == 5);
		assert(arr[0][1] == 2.55);
		assert(arr[1][2] == 5);
		assert(arr[1][4] == 8.8);
	}
	
	//tests augmenting a matrix with a column and a full matrix
	@Test public void test24() {
		Matrix m = new Matrix("5   6 7 8 ; 1 1 1 1");
		Matrix aug = m.augment(new Matrix(" 9 ; 1"));
		assert(aug.equals(new Matrix("5 6 7 8 9 ; 1 1 1 1 1")));
		
		aug = m.augment(new Matrix("1 1 1 ; 2 2 2"));
		assert(aug.equals(new Matrix("5 6 7 8 1 1 1 ; 1 1 1 1 2 2 2")));
	}
	
	/* the following are tests for errors/exceptions in Matrix methods */
	
	//tests using bad dimensions for a matrix
	@Test public void test25() {
		try {
			new Matrix(0, 5);
			fail();
		} catch (ArrayIndexOutOfBoundsException e) {}
		
		try {
			new Matrix(5, 0);
			fail();
		} catch (ArrayIndexOutOfBoundsException e) {}
		
		try {
			new Matrix(-1, -5);
			fail();
		} catch (ArrayIndexOutOfBoundsException e) {}
	}
	
	//tests improper arrays for the constructor
	@Test public void test26() {
		double[] arr1 = {1, 2.5, 3};
		double[] arr2 = {1,2};
		double[] arr3 = {1,2,5};
		try {
			new Matrix(new double[][] {arr1, arr2, arr3});
			fail();
		} catch (ArrayIndexOutOfBoundsException e) {}
		
		try {
			new Matrix(new double[][] {arr1, arr3, null});
			fail();
		} catch (ArrayIndexOutOfBoundsException e) {}
	}
	
	//tests improper Strings in the constructor
	@Test public void test27() {
		try {
			new Matrix("  ");
			fail();
		} catch (ArrayIndexOutOfBoundsException e) {}
		
		try {
			new Matrix("1 2 3; ; 5    ");
			fail();
		} catch (ArrayIndexOutOfBoundsException e) {}
		
		try {
			new Matrix("1 ; hello ; 5");
			fail();
		} catch (ArrayIndexOutOfBoundsException e) {}
		
		try {
			new Matrix("  1 2 3;");
			fail();
		} catch (ArrayIndexOutOfBoundsException e) {}
		
		try {
			new Matrix("  1 2 3; 5 - 6 7");
			fail();
		} catch (ArrayIndexOutOfBoundsException e) {}
		
		try {
			new Matrix("1 2 3 ; 5 -6");
			fail();
		} catch (ArrayIndexOutOfBoundsException e) {}
	}
	
	//tests errors when getting values, rows, and columns
	@Test public void test28() {
		Matrix m = new Matrix("1 2 3; 4 5 6");
		try {
			m.getValue(0, -1);
			fail();
		} catch (ArrayIndexOutOfBoundsException e) {}
		
		try {
			m.getValue(0, 3);
			fail();
		} catch (ArrayIndexOutOfBoundsException e) {}
		
		try {
			m.getValue(2, 1);
			fail();
		} catch (ArrayIndexOutOfBoundsException e) {}
		
		try {
			m.getValue(-4, 1);
			fail();
		} catch (ArrayIndexOutOfBoundsException e) {}
		
		assert(m.getRow(2) == null);		
		assert(m.getRow(-2) == null);
		assert(m.getColumn(3) == null);
		assert(m.getColumn(-2) == null);
	}
	
	//tests errors with setting a value
	@Test public void test29() {
		Matrix m = new Matrix("1 2 3 ; 4.5 6 7");
		try {
			m.setValue(2, 1, 0.5);
			fail();
		} catch (ArrayIndexOutOfBoundsException e) {}
		
		try {
			m.setValue(0, 3, 0.6);
			fail();
		} catch (ArrayIndexOutOfBoundsException e) {}
		
		try {
			m.setValue(-1, 1, 0.5);
			fail();
		} catch (ArrayIndexOutOfBoundsException e) {}
		
		try {
			m.setValue(1, -3, 0.5);
			fail();
		} catch (ArrayIndexOutOfBoundsException e) {}
	}
	
	//tests errors with setting rows and columns
	@Test public void test30() {
		Matrix m = new Matrix("1 2 3; 4 5 6 ; 7 8 9");
		Matrix row = new Matrix(" 0 0 0 ");
		Matrix col = new Matrix(" 0 ; 0 ;0");
		try {
			m.setRow(-1, row);
			fail();
		} catch (ArrayIndexOutOfBoundsException e) {}
		
		try {
			m.setRow(3, row);
			fail();
		} catch (ArrayIndexOutOfBoundsException e) {}
		
		try {
			m.setRow(0, new Matrix(" 0 0 0 0"));
			fail();
		} catch (ArrayIndexOutOfBoundsException e) {}
		
		try {
			m.setColumn(-1, col);
			fail();
		} catch (ArrayIndexOutOfBoundsException e) {}
		
		try {
			m.setColumn(3, col);
			fail();
		} catch (ArrayIndexOutOfBoundsException e) {}
		
		try {
			m.setColumn(0, new Matrix(" 0; 0 ;0; 0"));
			fail();
		} catch (ArrayIndexOutOfBoundsException e) {}
	}
	
	//tests adding and subtracting matrices improperly
	@Test public void test31() {
		assert((new Matrix("1 2 ; 3 4")).add(new Matrix("1 ; 2")) == null);
		assert((new Matrix("1 2 ; 3 4")).add(new Matrix("0 0 1; 2 2 1")) == null);
		assert((new Matrix("1 2 ; 3 4")).add(null) == null);
		assert((new Matrix("1 2 ; 3 4")).subtract(new Matrix("1 ; 2")) == null);
		assert((new Matrix("1 2 ; 3 4")).subtract(new Matrix("0 0 ; 2 2 ; 3 3")) == null);
		assert((new Matrix("1 2 ; 3 4")).subtract(null) == null);
	}
	
	//tests multiplying matrices improperly
	@Test public void test32() {
		assert((new Matrix("1 2 ; 3 4")).multiply(new Matrix("1 ; 2 ;4")) == null);
		assert((new Matrix("1 2 ; 3 4")).multiply(new Matrix("0 0 1; 2 2 1 ; 5 6 0")) == null);
		assert((new Matrix("1 2 ; 3 4")).multiply(null) == null);
	}
	
	//tests dividing a matrix by 0
	@Test public void test33() {
		assert((new Matrix("1 2 ; 3 4")).divide(0) == null);
	}
	
	//tests negative exponents and using non-square matrices
	@Test public void test34() {
		assert((new Matrix("1 2 ; 3 4")).toPower(-1) == null);
		assert((new Matrix("1 2 3; 3 4 0")).toPower(2) == null);
	}
	
	//tests comparing a matrix with null
	@Test public void test35() {
		assert((new Matrix("1 2 3")).equals(null) == false);
	}
	
	//tests augmenting matrices improperly
	@Test public void test36() {
		assert((new Matrix("1 2 3 ; 4 5 6")).augment(new Matrix("1 ; 2 ; 3")) == null);
		assert((new Matrix("1 2 3 ; 4 5 6")).augment(new Matrix("1 2")) == null);
		assert((new Matrix("1 2 3 ; 4 5 6")).augment(null) == null);
	}
}
