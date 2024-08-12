const { onDocumentCreated } = require("firebase-functions/v2/firestore");
const { defineString } = require("firebase-functions/params");
const { initializeApp } = require("firebase-admin/app");
initializeApp();

const { GoogleGenerativeAI } = require("@google/generative-ai");

const GEMINI_API_KEY = defineString("GEMINI_API_KEY");

exports.triage = onDocumentCreated("triaging/{docId}", (event) => {
  const snapshot = event.data;
  if (!snapshot) {
    console.log("No data associated with the event");
    return;
  }
  const data = snapshot.data();

  const genAI = new GoogleGenerativeAI(GEMINI_API_KEY.value());
  const model = genAI.getGenerativeModel({ model: "gemini-1.5-flash" });

  const generationConfig = {
    temperature: 1,
    topP: 0.95,
    topK: 64,
    maxOutputTokens: 8192,
    responseMimeType: "application/json",
  };

  /**
   * Sends a message to the chat session and updates the Firestore document based on the response.
   * The function sends a prompt to determine if a message is perceived as global or local based on its content.
   * It then logs the response and updates the Firestore document with the reach analysis result.
   *
   * @async
   * @function sendMessage
   * @param {string} prompt - The message prompt sent to the chat session. It asks the user to classify the message as "GLOBAL" or "LOCAL" based on its content.
   * @returns {Promise<void>} A promise that resolves when the message has been sent and the Firestore document has been updated.
   */
  async function run() {
    const chatSession = model.startChat({ generationConfig });

    const result = await chatSession.sendMessage(
      'I will give you a message and you have to tell me if for you it looks like it is addressed to a worldwide audience similar to a tweet, message or announcement on a public forum by saying "GLOBAL", or if it is meant to only be shared in a private / local context or to friends in an area, where you will say "LOCAL".\n\nAnswer must be a single word such as "GLOBAL" or "LOCAL".\n\nHere is a message:\n```' +
        data.text +
        "\n```"
    );
    console.log(result.response.text());

    snapshot.ref.update({ reach: result });
  }

  run();
});
