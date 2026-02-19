"""
String Reversal & Palindrome Check
Common interview string manipulation problems.
"""

def reverse_string(s):
    """Reverse a string using two pointers. O(n) time, O(n) space."""
    chars = list(s)
    left, right = 0, len(chars) - 1
    while left < right:
        chars[left], chars[right] = chars[right], chars[left]
        left += 1
        right -= 1
    return ''.join(chars)

def is_palindrome(s):
    """Check if string is palindrome (ignoring non-alphanumeric). O(n)."""
    cleaned = ''.join(c.lower() for c in s if c.isalnum())
    return cleaned == cleaned[::-1]

def longest_palindrome_substring(s):
    """Expand around center approach. O(n²) time, O(1) space."""
    if not s:
        return ''
    start, max_len = 0, 1

    def expand(left, right):
        nonlocal start, max_len
        while left >= 0 and right < len(s) and s[left] == s[right]:
            if right - left + 1 > max_len:
                start = left
                max_len = right - left + 1
            left -= 1
            right += 1

    for i in range(len(s)):
        expand(i, i)      # Odd length
        expand(i, i + 1)  # Even length

    return s[start:start + max_len]

# --- Examples ---
if __name__ == '__main__':
    print(reverse_string('hello'))                    # 'olleh'
    print(is_palindrome('A man, a plan, a canal: Panama'))  # True
    print(longest_palindrome_substring('babad'))        # 'bab' or 'aba'
    print(longest_palindrome_substring('cbbd'))         # 'bb'
