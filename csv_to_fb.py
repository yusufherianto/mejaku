#!/usr/bin/env python3

import pandas as pd
import firebase_admin
from firebase_admin import credentials, firestore

cred = credentials.Certificate('mejaku-test-firebase-adminsdk-hk1pg-03218b3fdf.json') //change to your own credential file

firebase_admin.initialize_app(cred, {
'databaseURL': 'https://mejaku.firebaseio.com/'
})

db = firestore.client()
doc_ref = db.collection(u'applications')

# Import data
df = pd.read_csv('data.csv', delimiter=';')
df['Predicted'] = 0
tmp = df.to_dict(orient='records')
list(map(lambda x: doc_ref.add(x), tmp))


# Export Data (To export all the documents (data) from the collection in the firebase dataset, simply use the `stream()` method)
docs = doc_ref.stream()
for doc in docs:
    print(f'{doc.id} => {doc.to_dict()}')