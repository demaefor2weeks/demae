const eventSource = new EventSource("/api/sse/connect");

eventSource.onmessage = function (event) {
	const notification = JSON.stringify(event.data);
	console.log(notification.message)
	alert(notification)
	window.location.href ="/api/orders"
};

console.log("EventSource created successfully.");