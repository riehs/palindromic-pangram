# Palindromic Pangram

Author: Daniel Riehs

This Java program searches for a series of words that contain all of the letters in the alphabet and reads the same backwards and forwards. The program will not work on any arbitrary list of words, and it makes a number of assumptions about words in the English language.

Here is the strategy that the program uses:

A palindrome can be constructed by taking a list of single-word palindromes and appending that same list in reverse order. Therefore, the first step involves checking to see if each letter in the alphabet can be found in a single-word palindrome.

We then loop through the letters that are not found in palindromes and search for multiple-word palindromes.

A word or sequence of words can be used to form a palindrome if a backwards sequence of words matches up with its letters.

Since an odd-length palindrome only uses a center letter once, we also check to see if a sequence can be found that does not include the last letter of the original sequence.

The program checks for these backwards sequences in one and two word phrases that include the sought-after letters.

When these new palindromes are found, they are added to a list of palindromes. Necessary single-word palindromes are then appending to the list and printed to the display.