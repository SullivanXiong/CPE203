; CSC 225, Assignment 7
; Sullivan Xiong (suxiong@calpoly.edu).
; Implements the "charCount" function for main.asm.
;
; R1 -> Pointer to *str.
; R2 -> Key to look for.
; R3 -> Temporary storage.
; R4 -> Negative Key.


        .ORIG x3300

        ST R1, STORER1  ; Store R1.

        ADD R6, R6, #-1 ; Push space for the return value.
        ADD R6, R6, #-1 ; Push the return address (R7).
        STR R7, R6, #0  ; Store the return address.
        ADD R6, R6, #-1 ; Push the dynamic link (R5).
        STR R5, R6, #0  ; Store the dynamic link.
        ADD R5, R6, #-1 ; Set the new frame pointer (R5).
        ADD R6, R6, #-2 ; Push local variables (normal order).

        LDR R0, R5, #4  ; Load str.
        LDR R0, R0, #0  ; Load value in str.
        BRz END1        ; If 0, LD values and end.
        BRnzp CONT
END1    AND R0, R0, #0  ; Clear R0.
        STR R0, R5, #0  ; result = 0.
        BRnzp END2
CONT    LDR R0, R5, #4  ; Load the value at str.
        LDR R0, R0, #0  ; Load the value at str.
        LDR R1, R5, #5  ; Load the key into R4.
        NOT R1, R1      ; Turn key into a negative.
        ADD R1, R1, #1  ;
        ADD R0, R0, R1  ; str[0] - key
        STR R0, R5, #-1 ; store result into match
        BRz MATCH       ; If 0 is 0 then it's a match.
        AND R0, R0, #0  ; Clear R0.
        STR R0, R5, #0  ; result = 0.
        BRnzp RECUR     ; Jump to recursive call.                
MATCH   AND R0, R0, #0  ; Clear R0.
        ADD R0, R0, #1  ; result = 1.
        STR R0, R5, #0  ;

        ; numChars = charCount(str, key)
RECUR   LDR R0, R5, #5      ; Load key into R0.
        ADD R6, R6, #-1     ; Push key argument.
        STR R0, R6, #0
        LDR R0, R5, #4      ; Load str into R0.
        ADD R0, R0, #1      ; Push str argument.
        ADD R6, R6, #-1
        STR R0, R6, #0

        LD R0, CCOUNT       ; Call charCount.
        JSRR R0

        LDR R0, R6, #0      ; Pop the return value into R0.
        ADD R6, R6, #1
        ADD R6, R6, #1      ; Pop str argument.
        ADD R6, R6, #1      ; Pop key argument.

        LDR R1, R5, #0      
        ADD R0, R1, R0      ; Add result by return value.
        STR R0, R5, #0      ;

END2    LD R1, STORER1  ; Load R1.

        LDR R0, R5, #0  ; Place return value in Stack Frame.
        STR R0, R5, #3
        ADD R6, R5, #1  ; Pop local variables.
        LDR R5, R6, #0  ; Pop the dynamic link (R5).
        ADD R6, R6, #1
        LDR R7, R6, #0  ; Pop the return address (R7).
        ADD R6, R6, #1
        RET             ; Exit subroutine with ret.

STORER1  .FILL x0
CCOUNT   .FILL x3300

        .END
