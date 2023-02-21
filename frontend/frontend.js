fs = require("fs")
prompt = require("prompt");

const responseString = (json) => {
  if (json.response.matrix) {
    return json.response.matrix.replaceAll("n", "<br>")
  }

  return json.response
}

const backendCall = async (command, backend) => {
  console.log(backend)
  const response = await fetch(`http://${backend}:4567/`, {
    credentials: 'same-origin', 
    method: 'POST',
    body: `{"command": "${command}"}`,
  })
  .then((res) => res.json())
  .then((json) => document.getElementById("output").innerHTML = responseString(json));
}



command = "5"
fs.readFile('backend_call.json', function(err, data) {
  console.log(data.backend)
});

