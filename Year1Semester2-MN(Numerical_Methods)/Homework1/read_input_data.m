% reads cluster count and points from input files 
function [NC points] = read_input_data(file_params, file_points)
	
	% set these values correctly
  % initializarea valorilor
	NC = 0;
	points = [];

	% TODO read NC
  NC=load(file_params, "-ascii");
	% TODO read points
  points=load(file_points, "-ascii");

 end

