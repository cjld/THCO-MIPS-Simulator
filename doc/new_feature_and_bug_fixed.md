*	修复 move 指令目标寄存器与来源寄存器颠倒问题
*	修复 JALR 指令编译 bug
*	LW, SW 的符号扩展问题
*	short 带符号整数访存越界
*	支持数值常量与字符串常量
*	支持全局 label, 使用代码如下

		B main

		string:
		"THCO-MIPS, hello world~ by Randon"

		main:
		LI R2 0xBF
		SLL R2 R2 0x0
		; 加载 string 的首地址
		LI R0 string
		; str for loop begin
		str_loop_start:
		LW R0 R1 0x0
		BEQZ R1 str_loop_end
			; loop content
			SW R2 R1 0x0
		ADDIU R0 0x1
		B str_loop_start
		str_loop_end:

