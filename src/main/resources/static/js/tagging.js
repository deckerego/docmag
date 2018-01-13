function previewSelect(image, selection) {
    var previewCanvas = document.getElementById('templatePreview');
    var canvasContext = previewCanvas.getContext('2d');
    previewCanvas.width = selection.width;
    previewCanvas.height = selection.height;
    canvasContext.drawImage(image, selection.x1, selection.y1, selection.width, selection.height, 0, 0, selection.width, selection.height);
}

function saveSelect(image, selection) {
    $('#templateX').val(selection.x1);
    $('#templateY').val(selection.y1);
    $('#templateWidth').val(selection.width);
    $('#templateHeight').val(selection.height);
}


