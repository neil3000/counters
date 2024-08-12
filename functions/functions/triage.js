const { onDocumentCreated } = require("firebase-functions/v2/firestore");
const { defineString } = require("firebase-functions/params");
const { initializeApp } = require("firebase-admin/app");
initializeApp();

const { GoogleGenerativeAI } = require("@google/generative-ai");

exports.triage = onDocumentCreated("triaging/{docId}", (event) => {
  const snapshot = event.data;
  if (!snapshot) {
    console.log("No data associated with the event");
    return;
  }
  const data = snapshot.data();

  const genAI = new GoogleGenerativeAI(defineString("GEMINI_API_KEY"););
  const model = genAI.getGenerativeModel({ model: "gemini-1.5-flash" });

  const generationConfig = {
    temperature: 1,
    topP: 0.95,
    topK: 64,
    maxOutputTokens: 8192,
    responseMimeType: "application/json",
  };

  async function run() {
    const chatSession = model.startChat({ generationConfig });

    const result = await chatSession.sendMessage("I will give you a message and you have to tell me if for you it looks like it is addressed to a worldwide audience similar to a tweet, message or announcement on a public forum by saying \"GLOBAL\", or if it is meant to only be shared in a private / local context or to friends in an area, where you will say \"LOCAL\".\n\nAnswer must be a single word such as \"GLOBAL\" or \"LOCAL\".\n\nHere is a message:\n```"+data.text+"\n```");
    console.log(result.response.text());

    snapshot.ref.update({ reach: result })
  }

  run();
});
