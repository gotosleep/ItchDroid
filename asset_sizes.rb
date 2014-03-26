#!/user/bin/ruby

size_raw = ARGV.length > 0 ? ARGV[0] : nil

if size_raw != nil
	pieces = size_raw.split(/x/)
	if pieces.length >= 2
		width = pieces[0].to_i
		height = pieces[1].to_i
	end
end

sizes = [
	{"name" => "mdpi", "scale" => 1},
	{"name" => "hdpi", "scale" => 1.5},
	{"name" => "xhdpi", "scale" => 2},
	{"name" => "xxhdpi", "scale" => 3},
	{"name" => "xxxhdpi", "scale" => 4}
]
sizes.each do |size|
	puts "#{size['name']}: #{(width*size['scale']).to_i}x#{(height*size['scale']).to_i}"
end

