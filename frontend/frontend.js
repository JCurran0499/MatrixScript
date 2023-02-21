const responseString = (json) => {
  if (json.response.matrix) {
    return json.response.matrix.replaceAll("n", "<br>").replaceAll(" ", "&nbsp;")
  }

  return json.response
}

const userAction = async (command) => {
  const response = await fetch('http://localhost:4567/', {
    credentials: 'same-origin', 
    method: 'POST',
    body: `{"command": "${command}"}`,
  })
  .then((res) => res.json())
  .then((json) => document.getElementById("output").innerHTML = responseString(json));
}

command = prompt("Enter command:")
userAction(command)

