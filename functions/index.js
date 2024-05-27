/**
 * Import function triggers from their respective submodules:
 *
 * const {onCall} = require("firebase-functions/v2/https");
 * const {onDocumentWritten} = require("firebase-functions/v2/firestore");
 *
 * See a full list of supported triggers at https://firebase.google.com/docs/functions
 */

const { onRequest } = require("firebase-functions/v2/https");

const { getFirestore, FieldValue } = require("firebase-admin/firestore");
const { initializeApp } = require("firebase-admin/app");
initializeApp();

const express = require("express");
const app = express();

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

// Expose Express API as a single Cloud Function:
exports.url = onRequest(app);
