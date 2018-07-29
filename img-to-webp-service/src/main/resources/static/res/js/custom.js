$("#quality-range").on('change input', function() {
    let value = $(this).val();
    $("#input-quality-visualizer").html(value);
});