const rangeSlider = document.getElementById('range-slider-price');

if (rangeSlider) {
    const min = document.getElementById('input-min-price').value;
    const max = document.getElementById('input-max-price').value;
    var sliderMin = parseInt(document.getElementById('input-max-price').min);
    var sliderMax = parseInt(document.getElementById('input-max-price').max);
	noUiSlider.create(rangeSlider, {
    start: [min, max],
		connect: true,
		step: 1,
    range: {
			'min': sliderMin,
            'max': sliderMax
    }
	});

	const input0 = document.getElementById('input-min-price');
	const input1 = document.getElementById('input-max-price');
	const inputs = [input0, input1];

	rangeSlider.noUiSlider.on('update', function(values, handle){
		inputs[handle].value = Math.round(values[handle]);
	});

	rangeSlider.noUiSlider.on('change', function() {
          document.getElementById('filter').submit();
        });

	const setRangeSlider = (i, value) => {
		let arr = [null, null];
		arr[i] = value;

		console.log(arr);

		rangeSlider.noUiSlider.set(arr);
	};

	inputs.forEach((el, index) => {
		el.addEventListener('change', (e) => {
			console.log(index);
			setRangeSlider(index, e.currentTarget.value);
		});
	});
}