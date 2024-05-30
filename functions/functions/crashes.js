const {
  onNewFatalIssuePublished,
} = require("firebase-functions/v2/alerts/crashlytics");
const logger = require("firebase-functions/logger");
const { defineString } = require("firebase-functions/params");

const DISCORD_WEBHOOK_URL = defineString("DISCORD_WEBHOOK_URL");
const GITLAB_PROJECT_ID = defineString("GITLAB_PROJECT_ID");
const GITLAB_PROJECT_PRIVATE_TOKEN = defineString(
  "GITLAB_PROJECT_PRIVATE_TOKEN"
);

exports.crashes = onNewFatalIssuePublished(async (event) => {
  const { id, title, subtitle, appVersion } = event.data.payload.issue;

  const crashlyticsId = id;
  const webhookUrl = DISCORD_WEBHOOK_URL.value();
  const gitlabProjectId = GITLAB_PROJECT_ID.value();
  const gitlabProjectPrivateToken = GITLAB_PROJECT_PRIVATE_TOKEN.value();

  if (!webhookUrl || !gitlabProjectId || !gitlabProjectPrivateToken) {
    throw new Error("RahNeil_N3:Error | Some env variables aren't set");
  }

  try {
    // STEP ONE -- Create a Gitlab issue
    const gitlabIssue = await fetch(
      `https://gitlab.com/api/v4/projects/${gitlabProjectId}/issues?title=Invstigate%20fatal%20issue&labels=bug,critical,Planned&description=https://counters.rahmouni.dev/report/${crashlyticsId}`,
      {
        method: "POST",
        headers: { "PRIVATE-TOKEN": gitlabProjectPrivateToken },
      }
    )
      .then((response) => response.json())
      .then((data) => data.iid);

    // STEP TWO -- Send message on Discord
    fetch(webhookUrl, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(
        // Here's what the Discord API supports in the payload:
        // https://discord.com/developers/docs/resources/webhook#execute-webhook-jsonform-params
        {
          embeds: [
            {
              title: `<a:tgyro:1040415247195979867>  New fatal issue in \`${appVersion}\``,
              description:
                "A new fatal issue just started occuring.\nNeeds investigating.\n.",
              color: "16711680",
              fields: [
                {
                  name: title,
                  value: `${subtitle}\n.`,
                },
                {
                  name: "<:gitlab:1245738671068549252>  Gitlab issue",
                  value: `[counters.rahmouni.dev/issue/${gitlabIssue}](https://counters.rahmouni.dev/issue/${gitlabIssue})`,
                },
                {
                  name: "<:firebase:1245740009730871318>  Report & logs",
                  value: `[counters.rahmouni.dev/report/${crashlyticsId}](https://counters.rahmouni.dev/report/${crashlyticsId})`,
                },
              ],
            },
          ],
          username: "rn3-crashes",
        }
      ),
    });
  } catch (error) {
    logger.error(`Unable to post fatal Crashlytics alert ${crashlyticsId}`, error);
  }
});
