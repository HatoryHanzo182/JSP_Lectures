document.addEventListener('DOMContentLoaded', function()
{
    M.Modal.init(document.querySelectorAll('.modal'),
        {
            opacity: 0.6,
            inDuration: 200,
            outDuration: 200
        });
});