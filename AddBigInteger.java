package com.smartdot.entity;

import java.util.Scanner;

public class AddBigInteger {
	static int N=100;
	static int a[] = new int[N];
	static int b[] = new int[N];
	static int c[] = new int[N+1];
	static String s1;
	static String s2;
	public static void main(String[] args){
		AddBigInteger demo = new AddBigInteger();
		demo.input();
		demo.add(a, b, c);
		demo.output();
	}
	
	private void output(){
		System.out.println("the result:");
		int flag = N;
		while(c[flag]==0){
			flag--;
			if(flag==-1){
				System.out.println("0");
				return;
			}
		}
		for(int i=flag;i>=0;i--){
			System.out.print(c[i]);
		}
		System.out.println();
	}
	
	private void add(int a[],int b[],int c[]){
		for(int i=0;i<N;i++){
			c[i] = a[i] + b[i];
		}
		for(int i=0;i<N;i++){
			c[i+1] += c[i]/10;
			c[i] = c[i]%10;
		}
	}
	
	private void input(){
		System.out.println("please input two bigInteger:");
		Scanner scanner = new Scanner(System.in);
		s1 = scanner.nextLine();
		s2 = scanner.nextLine();
		getDigit(s1,a);
		getDigit(s2,b);
	}
	private void getDigit(String s1,int a[]){
		int len = s1.length();
		for(int i=0;i<len;i++){
			a[i] = s1.charAt(len-i-1) - '0';
		}
	}
}
