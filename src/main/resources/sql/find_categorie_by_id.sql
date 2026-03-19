SELECT id, libelle
FROM categories
WHERE id = ?;
ALTER TABLE categories
ADD CONSTRAINT unique_libelle
UNIQUE (libelle);