var templateRegion = { "x1": 0, "y1": 0, "width": 0, "height": 0 }

function previewSelect(image, selection) {
    var previewCanvas = document.getElementById('templatePreview');
    var canvasContext = previewCanvas.getContext('2d');
    previewCanvas.width = selection.width;
    previewCanvas.height = selection.height;
    canvasContext.drawImage(image, selection.x1, selection.y1, selection.width, selection.height, 0, 0, selection.width, selection.height);
}

function saveSelect(image, selection) {
    templateRegion = selection;
}

function saveTemplate() {
    var name = $('#tagName').val();
    alert("Saving "+name+" at ("+templateRegion.x1+", "+templateRegion.y1+")");
}

