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

function loadSelect(image, selection) {
    selection.x1 = parseInt($('#templateX').val(), 10);
    selection.y1 = parseInt($('#templateY').val(), 10);
    selection.width = parseInt($('#templateWidth').val(), 10);
    selection.height = parseInt($('#templateHeight').val(), 10);

    var imageAreaSelect = $('#coverImage').imgAreaSelect({ instance: true });
    imageAreaSelect.setSelection(selection.x1, selection.y1, selection.x1 + selection.width, selection.y1 + selection.height);
    imageAreaSelect.setOptions({ show: true });
    imageAreaSelect.update();

    previewSelect(image, selection);
}