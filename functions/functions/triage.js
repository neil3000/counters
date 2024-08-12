const { onDocumentCreated } = require("firebase-functions/v2/firestore");
const { defineString } = require("firebase-functions/params");
const { initializeApp } = require("firebase-admin/app");
const { info, error } = require("firebase-functions/logger");
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

  const chatSession = model.startChat({ generationConfig });

  chatSession
    .sendMessage(
      'I will share you a message. This message will be shared on a social media, you have to categorized it returning me only capitalized words separated by a comma.\n\n First category, for you does this message can be send on the app : "SUCCESS", "NOTADAPTED", "NEEDPICTURE"\n\n Second category, the feed (page in witch it will be shared) : "PUBLIC", "FRIENDS", "EVENT"\n\nThird category, at which range should this message be shared : "GLOBAL" (Only for announcements in FRIENDS feed), "COUNTRY", "CITY", "STREET", "BUILDING". If you hesitate between 2 take the lowest, the message has to interest nearly everyone in the scope\n\nLast category, what type of Display does it need : "TEXT", "CONTACT","POLL" \n\nHere is a message:\n```' +
        data.text +
        "\n```" +
        '\n\nPlease respond in the following format: CATEGORY1, CATEGORY2, CATEGORY3, CATEGORY4. For example: NOTADAPTED, PUBLIC, GLOBAL, TEXT. No punctuation marks at all other than the commas and nothing before or after the categories, strictly like the example'
    )
    .then((res) => {
      const categories = res.response.text().replace(/["{}]/g, '').split(",");
      const analyse = categories[0].trim(); // First category: "SUCCESS", "NOTADAPTED", "NEEDPICTURE"
      const feed = categories[1].trim(); // Second category: "PUBLIC", "FRIENDS", "EVENT"
      const scope = categories[2].trim(); // Third category: "GLOBAL", "COUNTRY", "CITY", "STREET", "BUILDING"
      const type = categories[3].trim(); // Last category: "TEXT", "CONTACT", "POLL"

      snapshot.ref.update({ feed: feed, analyse: analyse, scope: scope, type: type, analysed: true });
      info("Gemini triaged as " + res.response.text());
    })
    .catch((e) => error(e));
});