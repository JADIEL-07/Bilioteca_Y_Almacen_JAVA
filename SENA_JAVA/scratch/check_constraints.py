import psycopg2
conn = psycopg2.connect(host='194.163.142.34', port='5437', database='biblioteca_db', user='Jadiel_Zz', password='12872Jadiel#')
cur = conn.cursor()

cur.execute("SELECT column_name, is_nullable, column_default FROM information_schema.columns WHERE table_name='items' AND is_nullable='NO' ORDER BY ordinal_position;")
print('Columnas NOT NULL en items:')
for r in cur.fetchall(): print(r)

print()
cur.execute('SELECT id, name FROM categories;')
print('Todas las Categorias:', cur.fetchall())
cur.execute('SELECT id, name FROM locations;')
print('Todas las Ubicaciones:', cur.fetchall())
cur.execute('SELECT id, name FROM statuses;')
print('Todos los Estados:', cur.fetchall())

print()
cur.execute("SELECT column_name, udt_name FROM information_schema.columns WHERE table_name='users' AND column_name='document_type';")
print('document_type col:', cur.fetchall())
cur.execute("SELECT e.enumlabel FROM pg_type t JOIN pg_enum e ON t.oid = e.enumtypid WHERE t.typname LIKE '%document%';")
print('Valores document_type enum:', cur.fetchall())

cur.close()
conn.close()


print()
cur.execute("""SELECT column_name, is_nullable, column_default 
FROM information_schema.columns 
WHERE table_name='users' AND is_nullable='NO'
ORDER BY ordinal_position;""")
print('Columnas NOT NULL en users:')
for r in cur.fetchall(): print(r)

cur.close()
conn.close()
