/**
 * JavaScript Array Methods — Comprehensive Cheatsheet
 * Quick reference for the most useful array operations.
 */

const users = [
  { id: 1, name: 'Alice', age: 30, role: 'admin' },
  { id: 2, name: 'Bob', age: 25, role: 'user' },
  { id: 3, name: 'Charlie', age: 35, role: 'user' },
  { id: 4, name: 'Diana', age: 28, role: 'admin' },
];

// --- Searching ---
const found = users.find(u => u.name === 'Bob');          // { id: 2, ... }
const index = users.findIndex(u => u.age > 30);           // 2
const exists = users.some(u => u.role === 'admin');        // true
const allAdults = users.every(u => u.age >= 18);           // true

// --- Filtering & Mapping ---
const admins = users.filter(u => u.role === 'admin');      // [Alice, Diana]
const names = users.map(u => u.name);                      // ['Alice', ...]
const flatNames = [['a', 'b'], ['c']].flat();             // ['a','b','c']
const mapped = users.flatMap(u => [u.name, u.role]);       // flat version

// --- Reducing ---
const totalAge = users.reduce((sum, u) => sum + u.age, 0); // 118
const grouped = users.reduce((acc, u) => {
  (acc[u.role] = acc[u.role] || []).push(u);
  return acc;
}, {});  // { admin: [...], user: [...] }

// --- Sorting ---
const byAge = [...users].sort((a, b) => a.age - b.age);
const byName = [...users].sort((a, b) => a.name.localeCompare(b.name));

// --- Other useful methods ---
const unique = [...new Set([1, 2, 2, 3, 3])];              // [1, 2, 3]
const chunked = Array.from({ length: 3 }, (_, i) =>
  users.slice(i * 2, (i + 1) * 2)
); // Chunk into pairs

console.log({ found, admins, totalAge, grouped, unique });
