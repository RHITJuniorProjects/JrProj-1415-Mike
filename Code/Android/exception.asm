# File:        exception.asm
# Written by:  J.P. Mellor, 15 Sep. 2004
#
# This file demonstrates exception handling in mips
				
	.globl main		# Make main globl so you can refer to
				# it by name in SPIM.

	.text			# Text section of the program
				# (as opposed to data).

main:				# Program starts at main.
	li  	$t0, 0x7FFFFFFF	# a big number

	la	$t3, try
	lw	$t2, 0($t3)


	la $t7 try		#set try to 1
	li $t1 1		
	sw $t1 0($t7)
	bne	$t2, $0, skip
	addi	$t1, $t0, 2	# make it overflow

	skip:

	ori 	$v0, $0, 10	# Prepare to exit
	syscall			#   ... Exit.

