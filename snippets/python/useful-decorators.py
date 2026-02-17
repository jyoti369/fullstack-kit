"""
Useful Python Decorators Collection
Production-ready decorators for common patterns.
"""

import time
import functools
from typing import Any, Callable


def timer(func: Callable) -> Callable:
    """Measure and print execution time of a function."""
    @functools.wraps(func)
    def wrapper(*args, **kwargs):
        start = time.perf_counter()
        result = func(*args, **kwargs)
        elapsed = time.perf_counter() - start
        print(f'{func.__name__} took {elapsed:.4f}s')
        return result
    return wrapper


def retry(max_attempts: int = 3, delay: float = 1.0):
    """Retry a function on exception with exponential backoff."""
    def decorator(func: Callable) -> Callable:
        @functools.wraps(func)
        def wrapper(*args, **kwargs):
            for attempt in range(1, max_attempts + 1):
                try:
                    return func(*args, **kwargs)
                except Exception as e:
                    if attempt == max_attempts:
                        raise
                    wait = delay * (2 ** (attempt - 1))
                    print(f'Attempt {attempt} failed: {e}. Retrying in {wait}s...')
                    time.sleep(wait)
        return wrapper
    return decorator


def memoize(func: Callable) -> Callable:
    """Simple memoization decorator with cache."""
    cache = {}
    @functools.wraps(func)
    def wrapper(*args):
        if args not in cache:
            cache[args] = func(*args)
        return cache[args]
    wrapper.cache = cache
    wrapper.cache_clear = cache.clear
    return wrapper


def singleton(cls):
    """Ensure only one instance of a class exists."""
    instances = {}
    @functools.wraps(cls)
    def get_instance(*args, **kwargs):
        if cls not in instances:
            instances[cls] = cls(*args, **kwargs)
        return instances[cls]
    return get_instance


# --- Examples ---
if __name__ == '__main__':
    @timer
    def slow_function():
        time.sleep(0.1)
        return 'done'

    @memoize
    def fibonacci(n):
        if n < 2: return n
        return fibonacci(n - 1) + fibonacci(n - 2)

    slow_function()
    print(fibonacci(30))  # 832040 — instant with memoization
