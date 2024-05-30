const { onRequest } = require("firebase-functions/v2/https");

const { getFirestore, FieldValue } = require("firebase-admin/firestore");
const { initializeApp } = require("firebase-admin/app");
initializeApp();

const express = require("express");
const app = express();

app.get("/issue/:id", (req, res) => {
  res.status(301).redirect(`https://gitlab.com/neil3000/counters/-/issues/${req.params.id}`);
});

app.get("/report/:id", (req, res) => {
  res.status(301).redirect(`https://console.firebase.google.com/project/rahneil-n3-counters/crashlytics/app/android:rahmouni.neil.counters/issues/${req.params.id}`);
});

app.get("/:id", (req, res) => {
  var doc = getFirestore().collection("links").doc(req.params.id);

  doc.get().then((snapshot) => {
    if (snapshot.exists) {
      doc.update({ clicks: FieldValue.increment(1) });

      res.status(301).redirect(snapshot.data().redirectUrl);
    } else {
      res.send(404);
    }
  });
});

exports.url = onRequest(app);
