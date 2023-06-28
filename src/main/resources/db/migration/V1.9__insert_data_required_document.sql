INSERT INTO required_document(document_type_id, offer_id)
SELECT dt.id, o.id 
FROM document_type dt, offer o
WHERE (o.id BETWEEN 1 AND 24) AND (dt.id IN (1, 2, 3, 4));

INSERT INTO required_document(document_type_id, offer_id)
SELECT dt.id, o.id 
FROM document_type dt, offer o
WHERE (o.id BETWEEN 25 AND 44) AND (dt.id IN (1, 5, 6, 7));

INSERT INTO required_document(document_type_id, offer_id)
SELECT dt.id, o.id 
FROM document_type dt, offer o
WHERE (o.id BETWEEN 45 AND 57) AND (dt.id IN (1, 5, 7));

INSERT INTO required_document(document_type_id, offer_id)
SELECT dt.id, o.id 
FROM document_type dt, offer o
WHERE (o.id BETWEEN 58 AND 76) AND (dt.id IN (1, 7, 8, 9));

INSERT INTO required_document(document_type_id, offer_id)
SELECT dt.id, o.id 
FROM document_type dt, offer o
WHERE (o.id BETWEEN 77 AND 96) AND (dt.id IN (1, 8, 9, 10));

INSERT INTO required_document(document_type_id, offer_id)
SELECT dt.id, o.id 
FROM document_type dt, offer o
WHERE (o.id BETWEEN 97 AND 116) AND (dt.id IN (1, 11));
