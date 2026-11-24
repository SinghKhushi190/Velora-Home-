package com.example.data.catalog

import com.example.data.model.CraftedHighlight
import com.example.data.model.DesignIdea
import com.example.data.model.LightingConcept
import com.example.data.model.LightingSeries
import com.example.data.model.RoomCategory

object VeloraCatalog {

    const val HERO_IMAGE_URL =
        "https://images.unsplash.com/photo-1615874959474-d609969a20ed?w=900&auto=format&fit=crop"

    val craftedHighlights: List<CraftedHighlight> = listOf(
        CraftedHighlight(
            id = "crafted_timber",
            title = "Timber-Light",
            description = "Wooden false ceilings paired with warm cove lighting create a natural, calming glow throughout the bar and lounge area.",
            imageUrl = "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=700&auto=format&fit=crop",
            tag = "Signature Finish"
        ),
        CraftedHighlight(
            id = "crafted_staircase",
            title = "Quietly Elegant",
            description = "Cantilevered floating staircases defined by subtle integrated LED step lighting for a refined architectural statement.",
            imageUrl = "https://images.unsplash.com/photo-1600607687939-ce8a6c25118c?w=700&auto=format&fit=crop",
            tag = "Architecture"
        ),
        CraftedHighlight(
            id = "crafted_pendant",
            title = "Pendant Light",
            description = "Biophilic pendant lighting blends warm 2700K illumination with indoor greenery for an intimate, welcoming atmosphere.",
            imageUrl = "https://images.unsplash.com/photo-1615873968403-89e068629265?w=700&auto=format&fit=crop",
            tag = "Biophilic"
        )
    )

    val galleryImages: List<String> = listOf(
        "https://images.unsplash.com/photo-1616486029423-aaa4789e8c9a?w=700&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1618220179428-22790b461013?w=700&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1615874959474-d609969a20ed?w=700&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1600210492486-724fe5c67fb0?w=700&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1631679706909-1844bbd07221?w=700&auto=format&fit=crop",
        "https://images.unsplash.com/photo-1567016432779-094069958ea5?w=700&auto=format&fit=crop"
    )

    val designIdeas: List<DesignIdea> = listOf(
        // MODULAR KITCHENS
        DesignIdea(
            id = "kitchen_island_onyx",
            title = "Champagne Island Modular Kitchen",
            subtitle = "Seamless stone island with warm under-counter glow",
            category = RoomCategory.KITCHENS,
            styleTag = "Island • Satin Brass",
            dimensions = "14 × 12 ft",
            estimatedBudget = "₹4.8L – ₹6.5L",
            imageUrl = "https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=900&auto=format&fit=crop",
            description = "An open-plan modular kitchen anchored by a monolithic quartz waterfall island, handleless champagne-matte shutters, and integrated soffit task downlights.",
            lightingPairing = "Soffit Downlighting + Linear Trough under overhead cabinets",
            materials = listOf("HDHMR Satin Acrylic", "Statuario Quartz", "Brushed Brass Profiles", "Soft-Close Blum Channels"),
            isFeatured = true
        ),
        DesignIdea(
            id = "kitchen_l_shaped_walnut",
            title = "Warm Walnut L-Shaped Kitchen",
            subtitle = "Ergonomic work triangle with fluted glass loft cabinets",
            category = RoomCategory.KITCHENS,
            styleTag = "L-Shaped • Timber & Cream",
            dimensions = "10 × 11 ft",
            estimatedBudget = "₹3.1L – ₹4.2L",
            imageUrl = "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=900&auto=format&fit=crop",
            description = "Designed for modern Indian apartments, pairing moisture-resistant warm walnut laminates with illuminated fluted glass crockery uppers and tall appliance pantry.",
            lightingPairing = "Warm 3000K Strip Under-Cabinet + Recessed Ceiling Spots",
            materials = listOf("Marine Ply Carcass", "Fluted Tinted Glass", "Kalinga Stone Counter", "Matte Black Pulls")
        ),
        DesignIdea(
            id = "kitchen_parallel_sage",
            title = "Parallel Matte Ivory & Brass Kitchen",
            subtitle = "Dual-galley storage efficiency with concealed chimney hood",
            category = RoomCategory.KITCHENS,
            styleTag = "Parallel • Modern Minimalist",
            dimensions = "12 × 8 ft",
            estimatedBudget = "₹2.9L – ₹3.9L",
            imageUrl = "https://images.unsplash.com/photo-1565183928294-7063f23ce0f8?w=900&auto=format&fit=crop",
            description = "Maximises every inch of a compact galley kitchen with deep corner carousel units, pull-out oil tallboy, and crisp warm cove ceiling light.",
            lightingPairing = "Artificial Skylight Diffuser + Toe-Kick Motion LED",
            materials = listOf("PU Matte Finish", "Terrazzo Backsplash", "Granite Wet Counter", "SS 304 Baskets")
        ),

        // LIVING ROOMS
        DesignIdea(
            id = "living_travertine_lounge",
            title = "Travertine & Gold Cove Living Room",
            subtitle = "Floating TV wall with wall-grazed textured plaster",
            category = RoomCategory.LIVING_ROOM,
            styleTag = "Contemporary Luxury",
            dimensions = "18 × 14 ft",
            estimatedBudget = "₹4.5L – ₹6.2L",
            imageUrl = "https://images.unsplash.com/photo-1600210492486-724fe5c67fb0?w=900&auto=format&fit=crop",
            description = "A serene living sanctuary featuring a bespoke travertine-clad TV console, concealed linear ceiling troughs, and deep bouclé modular seating.",
            lightingPairing = "Linear Trough Lighting + Wall Grazing on Limewash Feature Wall",
            materials = listOf("Italian Travertine Veneer", "Bouclé Upholstery", "Brushed Gold Inlay", "Acoustic Timber Slats"),
            isFeatured = true
        ),
        DesignIdea(
            id = "living_coffered_grand",
            title = "Coffered Ceiling Heritage Salon",
            subtitle = "Layered 3D architectural coffer lighting & plush velvet",
            category = RoomCategory.LIVING_ROOM,
            styleTag = "Neoclassical • Coffered",
            dimensions = "20 × 16 ft",
            estimatedBudget = "₹5.5L – ₹7.8L",
            imageUrl = "https://images.unsplash.com/photo-1631679706909-1844bbd07221?w=900&auto=format&fit=crop",
            description = "Grand entertaining lounge crafted with coffered gypsum ceiling trays, recessed gold beading, and sculptural crystal droplet pendants.",
            lightingPairing = "Coffered Ceiling Lights + Torchière Corner Uplighters",
            materials = listOf("Gypsum Coffered Grid", "Smoked Oak Veneer", "Silk-Blend Velvet", "Antique Brass Hardware")
        ),
        DesignIdea(
            id = "living_biophilic_sunlit",
            title = "Biophilic Timber & Linen Living Room",
            subtitle = "Soft daylight palette with rattan pendants & indoor greens",
            category = RoomCategory.LIVING_ROOM,
            styleTag = "Organic Modern",
            dimensions = "15 × 12 ft",
            estimatedBudget = "₹3.4L – ₹4.6L",
            imageUrl = "https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=900&auto=format&fit=crop",
            description = "Airy sunlit living room anchored by woven rattan fixtures, low-profile ash wood media console, and warm valance drapery glow.",
            lightingPairing = "Valance Lighting + Rattan & Wicker Pendant Shades",
            materials = listOf("White Ash Veneer", "Belgian Linen", "Handwoven Cane", "Matte Lime Paint")
        ),

        // BEDROOMS
        DesignIdea(
            id = "bedroom_sanctuary_cove",
            title = "Amber Cove Master Bedroom Suite",
            subtitle = "Full-height upholstered headboard with backlit niches",
            category = RoomCategory.BEDROOM,
            styleTag = "Modern Sanctuary",
            dimensions = "14 × 13 ft",
            estimatedBudget = "₹3.6L – ₹4.9L",
            imageUrl = "https://images.unsplash.com/photo-1615874959474-d609969a20ed?w=900&auto=format&fit=crop",
            description = "Designed for restorative sleep with circadian warm-to-amber bedside lighting, suede-tufted wall panelling, and floating side tables.",
            lightingPairing = "Circadian Tunable Lighting + Plug-in Swing Arm Reading Lamps",
            materials = listOf("Microsuede Panelling", "Teak Veneer", "Bronze Mirror Strips", "Memory Foam Tufting"),
            isFeatured = true
        ),
        DesignIdea(
            id = "bedroom_floating_ceiling",
            title = "Floating Ceiling Penthouse Bedroom",
            subtitle = "Levitating perimeter ceiling halo & walnut study nook",
            category = RoomCategory.BEDROOM,
            styleTag = "Minimalist Luxury",
            dimensions = "16 × 14 ft",
            estimatedBudget = "₹4.2L – ₹5.8L",
            imageUrl = "https://images.unsplash.com/photo-1540518614846-7eded433c457?w=900&auto=format&fit=crop",
            description = "A calm monochrome-and-gold bedroom where a suspended ceiling island floats in soft warm LED light above a bespoke king platform bed.",
            lightingPairing = "Floating Ceiling Bands + Fibre Optic Starlight Accent",
            materials = listOf("American Walnut", "Satin Champagne Lacquer", "Linen Drapery", "Brushed Brass Trim")
        ),
        DesignIdea(
            id = "bedroom_guest_warm_cream",
            title = "Warm Cream & Cane Guest Bedroom",
            subtitle = "Compact smart storage bed with arch bedside sconces",
            category = RoomCategory.BEDROOM,
            styleTag = "Warm Contemporary",
            dimensions = "11 × 11 ft",
            estimatedBudget = "₹2.4L – ₹3.3L",
            imageUrl = "https://images.unsplash.com/photo-1505873242700-f289a29e1e0f?w=900&auto=format&fit=crop",
            description = "Welcoming guest suite combining hydraulic storage under the bed, cane-inlay dresser, and soft architectural niche lighting.",
            lightingPairing = "Illuminated Niches + Warm 2700K Brass Sconces",
            materials = listOf("Natural Rattan Cane", "Ivory Laminate", "Oak Flooring", "Soft Cotton Upholstery")
        ),

        // WARDROBES
        DesignIdea(
            id = "wardrobe_fluted_walkin",
            title = "Fluted Glass Walk-In Dressing Studio",
            subtitle = "Sensor-lit shelves, jewellery island & bronze frames",
            category = RoomCategory.WARDROBES,
            styleTag = "Walk-In • Bronze Glass",
            dimensions = "10 × 8 ft",
            estimatedBudget = "₹3.5L – ₹5.0L",
            imageUrl = "https://images.unsplash.com/photo-1618221195710-dd6b41faaea6?w=900&auto=format&fit=crop",
            description = "Boutique-inspired walk-in closet with slim champagne-anodised aluminium frames, fluted bronze glass shutters, and motion-activated shelf strips.",
            lightingPairing = "Motion-Activated Vertical Profile LEDs + Spot Track",
            materials = listOf("Fluted Bronze Glass", "Anodised Champagne Aluminium", "Velvet-Lined Drawers", "Smoked Oak"),
            isFeatured = true
        ),
        DesignIdea(
            id = "wardrobe_sliding_matte",
            title = "Floor-to-Ceiling Sliding Mirror Wardrobe",
            subtitle = "Space-saving loft wardrobe for urban bedrooms",
            category = RoomCategory.WARDROBES,
            styleTag = "Sliding • Lacquered Glass",
            dimensions = "8 × 9 ft",
            estimatedBudget = "₹1.8L – ₹2.6L",
            imageUrl = "https://images.unsplash.com/photo-1598928506311-c55ded91a20c?w=900&auto=format&fit=crop",
            description = "Sleek sliding doors in warm taupe lacquered glass paired with tinted mirror panels that amplify light and room depth.",
            lightingPairing = "Internal Hanging Rod LED + Recessed Ceiling Wash",
            materials = listOf("Back-Painted Lacquered Glass", "Tinted Mirror", "Hettich Top-Line Track", "Linen Textured Carcass")
        ),

        // BATHROOMS
        DesignIdea(
            id = "bath_onyx_spa",
            title = "Backlit Onyx & Floating Vanity Spa",
            subtitle = "Infinity mirror halo & separated glass wet zone",
            category = RoomCategory.BATHROOMS,
            styleTag = "Luxury Spa",
            dimensions = "9 × 7 ft",
            estimatedBudget = "₹2.6L – ₹3.8L",
            imageUrl = "https://images.unsplash.com/photo-1555041469-a586c61ea9bc?w=900&auto=format&fit=crop",
            description = "Resort-grade master bathroom featuring a backlit honey onyx vanity wall, matte gold concealed diverters, and anti-fog mirror infinity lighting.",
            lightingPairing = "Backlit Onyx Panels + Mirror Infinity Lights (IP65)",
            materials = listOf("Translucent Honey Onyx", "Porcelain Terrazzo", "PVD Brushed Gold Fittings", "Toughened Clear Glass")
        ),
        DesignIdea(
            id = "bath_compact_stone",
            title = "Warm Stone Compact Powder Room",
            subtitle = "Fluted stone basin with recessed niche illumination",
            category = RoomCategory.BATHROOMS,
            styleTag = "Powder Room • Sculptural",
            dimensions = "6 × 5 ft",
            estimatedBudget = "₹1.5L – ₹2.2L",
            imageUrl = "https://images.unsplash.com/photo-1571508601891-ca5e7a713859?w=900&auto=format&fit=crop",
            description = "High-impact guest powder room using a monolithic fluted stone pedestal basin and dramatic downward wall-grazing light.",
            lightingPairing = "Illuminated Niches + Candelabra Warm Sconces",
            materials = listOf("Honed Limestone", "Waterproof PU Vanity", "Antique Brass Tapware", "Bevelled Mirror")
        ),

        // DINING & BAR
        DesignIdea(
            id = "dining_timber_bar",
            title = "Timber-Light Home Bar & Dining",
            subtitle = "Suspended bubble cluster above walnut marble table",
            category = RoomCategory.DINING_BAR,
            styleTag = "Dining & Bar • Statement",
            dimensions = "15 × 12 ft",
            estimatedBudget = "₹3.9L – ₹5.4L",
            imageUrl = "https://images.unsplash.com/photo-1615873968403-89e068629265?w=900&auto=format&fit=crop",
            description = "Inspired by Velora Home's signature Timber-Light concept: slatted teak ceiling with warm cove glow, backlit bottle display, and 6-seater marble dining.",
            lightingPairing = "Bubble Cluster Lights + Timber-Light False Ceiling Cove",
            materials = listOf("Burma Teak Slats", "Nero Marquina Marble", "Tinted Brass Mesh", "Dimmable 2700K Pendants"),
            isFeatured = true
        ),
        DesignIdea(
            id = "dining_sputnik_crockery",
            title = "Sputnik Dining & Illuminated Crockery Unit",
            subtitle = "Mid-century gold chandelier over solid wood dining",
            category = RoomCategory.DINING_BAR,
            styleTag = "Mid-Century Gold",
            dimensions = "13 × 11 ft",
            estimatedBudget = "₹2.8L – ₹3.9L",
            imageUrl = "https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=900&auto=format&fit=crop",
            description = "Warm dining alcove anchored by a radiating Sputnik brass chandelier and tall glass-front crockery cabinet with warm interior backlighting.",
            lightingPairing = "Sputnik Chandeliers + Cabinet Shelf Spotlights",
            materials = listOf("Solid Sheesham", "Brushed Brass Arms", "Clear Fluted Glass", "Textured Cream Wallpaper")
        ),

        // STUDY & FOYERS
        DesignIdea(
            id = "foyer_pooja_sanctuary",
            title = "Backlit Jaali Pooja & Entry Foyer",
            subtitle = "Brass bells, carved niche & antique lantern warmth",
            category = RoomCategory.STUDY_FOYER,
            styleTag = "Pooja & Foyer • Heritage",
            dimensions = "8 × 6 ft",
            estimatedBudget = "₹1.6L – ₹2.4L",
            imageUrl = "https://images.unsplash.com/photo-1519710164239-da123dc03ef4?w=900&auto=format&fit=crop",
            description = "A serene entry foyer transitioning into a warm backlit CNC-carved corian pooja mandir with concealed brass bells and drawers.",
            lightingPairing = "Antique Lanterns + Backlit Translucent Corian Panel",
            materials = listOf("Backlit Corian", "Teak Veneer Framing", "Handcrafted Brass Bells", "Italian Marble Pedestal")
        ),
        DesignIdea(
            id = "study_executive_oak",
            title = "Executive Oak Home Office & Library",
            subtitle = "Acoustic fluted wall, glare-free linear task desk",
            category = RoomCategory.STUDY_FOYER,
            styleTag = "Home Office • Focused",
            dimensions = "11 × 10 ft",
            estimatedBudget = "₹2.3L – ₹3.2L",
            imageUrl = "https://images.unsplash.com/photo-1524758631624-e2822e304c36?w=900&auto=format&fit=crop",
            description = "Work-from-home sanctuary built with floor-to-ceiling book shelving, acoustic felt backing, and flicker-free circadian task illumination.",
            lightingPairing = "Directional Spotlights + Circadian Desk Task Lamp",
            materials = listOf("Smoked Oak Laminate", "Acoustic Wool Felt", "Matte Leather Desk Pad", "Concealed Cable Trays")
        )
    )

    val lightingConcepts: List<LightingConcept> = listOf(
        // 01 ARCHITECTURAL
        LightingConcept(
            id = "arch_linear_trough",
            series = LightingSeries.ARCHITECTURAL,
            numberCode = "01",
            tag = "Trough",
            title = "Linear Trough Lighting",
            description = "Long recessed channels cut into ceilings or walls project a continuous band of diffused light that defines spatial boundaries with architectural precision.",
            imageUrl = "https://images.unsplash.com/photo-1600210492486-724fe5c67fb0?w=800&auto=format&fit=crop",
            idealFor = "Living rooms, corridors & open-plan ceilings",
            colorTemp = "3000K Warm White"
        ),
        LightingConcept(
            id = "arch_coffered",
            series = LightingSeries.ARCHITECTURAL,
            numberCode = "02",
            tag = "Coffer",
            title = "Coffered Ceiling Lights",
            description = "Lighting integrated inside coffered ceiling panels creates a layered, three-dimensional glow unique to grand interiors.",
            imageUrl = "https://images.unsplash.com/photo-1631679706909-1844bbd07221?w=800&auto=format&fit=crop",
            idealFor = "Formal dining halls & master lounges",
            colorTemp = "2700K Soft Gold"
        ),
        LightingConcept(
            id = "arch_grazing",
            series = LightingSeries.ARCHITECTURAL,
            numberCode = "03",
            tag = "Grazing",
            title = "Wall Grazing",
            description = "Fixtures placed close to textured walls skim light across the surface, dramatically revealing stone, brick, or plaster relief.",
            imageUrl = "https://images.unsplash.com/photo-1555041469-a586c61ea9bc?w=800&auto=format&fit=crop",
            idealFor = "Stone feature walls, foyers & staircases",
            colorTemp = "2700K–3000K"
        ),
        LightingConcept(
            id = "arch_niches",
            series = LightingSeries.ARCHITECTURAL,
            numberCode = "04",
            tag = "Niche",
            title = "Illuminated Niches",
            description = "Recessed alcoves lit from within become glowing architectural jewels — ideal for sculptures, vessels, and curated objects.",
            imageUrl = "https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=800&auto=format&fit=crop",
            idealFor = "Pooja units, foyer alcoves & bookshelves",
            colorTemp = "2700K Warm Accent"
        ),
        LightingConcept(
            id = "arch_valance",
            series = LightingSeries.ARCHITECTURAL,
            numberCode = "05",
            tag = "Valance",
            title = "Valance Lighting",
            description = "A horizontal board mounted high on the wall conceals light sources that wash both ceiling and drapery simultaneously.",
            imageUrl = "https://images.unsplash.com/photo-1524758631624-e2822e304c36?w=800&auto=format&fit=crop",
            idealFor = "Curtain pelmets & headboard walls",
            colorTemp = "2700K Indirect Glow"
        ),
        LightingConcept(
            id = "arch_soffit",
            series = LightingSeries.ARCHITECTURAL,
            numberCode = "06",
            tag = "Soffit",
            title = "Soffit Downlighting",
            description = "Lights built into the underside of soffits create clean directional task light in kitchens and corridors.",
            imageUrl = "https://images.unsplash.com/photo-1618221195710-dd6b41faaea6?w=800&auto=format&fit=crop",
            idealFor = "Modular kitchens & gallery hallways",
            colorTemp = "4000K Neutral Task"
        ),
        LightingConcept(
            id = "arch_onyx",
            series = LightingSeries.ARCHITECTURAL,
            numberCode = "07",
            tag = "Backlit",
            title = "Backlit Onyx Panels",
            description = "Translucent stone panels — onyx, alabaster, or textured glass — backlit with warm LEDs glow like living tapestries on feature walls.",
            imageUrl = "https://images.unsplash.com/photo-1556909114-f6e7ad7d3136?w=800&auto=format&fit=crop",
            idealFor = "Home bar counters, spa baths & foyers",
            colorTemp = "3000K CRI 95+"
        ),
        LightingConcept(
            id = "arch_column",
            series = LightingSeries.ARCHITECTURAL,
            numberCode = "08",
            tag = "Column",
            title = "Column Uplighters",
            description = "Fixtures set at the base of columns wash upward in dramatic pools, accentuating vertical architectural form.",
            imageUrl = "https://images.unsplash.com/photo-1540518614846-7eded433c457?w=800&auto=format&fit=crop",
            idealFor = "Double-height villas & covered verandas",
            colorTemp = "2700K Narrow Beam"
        ),
        LightingConcept(
            id = "arch_skylight",
            series = LightingSeries.ARCHITECTURAL,
            numberCode = "09",
            tag = "Sky",
            title = "Artificial Skylights",
            description = "LED panels behind frosted glass simulate soft, even natural daylight — ideal for basements and rooms without windows.",
            imageUrl = "https://images.unsplash.com/photo-1567016432779-094069958ea5?w=800&auto=format&fit=crop",
            idealFor = "Walk-in closets, bathrooms & inner kitchens",
            colorTemp = "5000K–6500K Daylight"
        ),
        LightingConcept(
            id = "arch_floating",
            series = LightingSeries.ARCHITECTURAL,
            numberCode = "10",
            tag = "Float",
            title = "Floating Ceiling Bands",
            description = "A suspended ceiling panel hovers above with concealed lighting — creating the illusion that the ceiling levitates in light.",
            imageUrl = "https://images.unsplash.com/photo-1505873242700-f289a29e1e0f?w=800&auto=format&fit=crop",
            idealFor = "Master bedrooms & minimalist lounges",
            colorTemp = "2700K Dimmable"
        ),

        // 02 DECORATIVE
        LightingConcept(
            id = "dec_lanterns",
            series = LightingSeries.DECORATIVE,
            numberCode = "01",
            tag = "Heritage",
            title = "Antique Lanterns",
            description = "Brass or iron cage lanterns carry old-world warmth into modern hallways and foyers with timeless presence.",
            imageUrl = "https://images.unsplash.com/photo-1519710164239-da123dc03ef4?w=700&auto=format&fit=crop",
            idealFor = "Entry foyers & double-height stairwells",
            colorTemp = "2200K Vintage Filament"
        ),
        LightingConcept(
            id = "dec_sputnik",
            series = LightingSeries.DECORATIVE,
            numberCode = "02",
            tag = "Retro-Modern",
            title = "Sputnik Chandeliers",
            description = "Radiating arms tipped with globe bulbs — a mid-century icon that injects sculptural energy into dining rooms.",
            imageUrl = "https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=700&auto=format&fit=crop",
            idealFor = "6 & 8-seater dining tables",
            colorTemp = "2700K Opal Globe"
        ),
        LightingConcept(
            id = "dec_bubble",
            series = LightingSeries.DECORATIVE,
            numberCode = "03",
            tag = "Cluster",
            title = "Bubble Cluster Lights",
            description = "Transparent glass spheres clustered at varying heights create an ethereal, cloud-like installation above dining areas.",
            imageUrl = "https://images.unsplash.com/photo-1565183928294-7063f23ce0f8?w=700&auto=format&fit=crop",
            idealFor = "Dining islands & stairwell voids",
            colorTemp = "2700K Warm Clear"
        ),
        LightingConcept(
            id = "dec_rattan",
            series = LightingSeries.DECORATIVE,
            numberCode = "04",
            tag = "Natural",
            title = "Rattan & Wicker Shades",
            description = "Woven natural fibre shades project intricate shadow patterns across walls and ceilings, bringing warmth and craft.",
            imageUrl = "https://images.unsplash.com/photo-1558618666-fcd25c85cd64?w=700&auto=format&fit=crop",
            idealFor = "Sunlit living rooms & covered balconies",
            colorTemp = "2700K Soft Ambient"
        ),
        LightingConcept(
            id = "dec_geometric",
            series = LightingSeries.DECORATIVE,
            numberCode = "05",
            tag = "Geometric",
            title = "Faceted Metal Pendants",
            description = "Angular copper or brass geometric shades play with light reflection, casting faceted patterns that animate ceilings.",
            imageUrl = "https://images.unsplash.com/photo-1598928506311-c55ded91a20c?w=700&auto=format&fit=crop",
            idealFor = "Kitchen islands & breakfast bars",
            colorTemp = "3000K Crisp Accent"
        ),
        LightingConcept(
            id = "dec_neon",
            series = LightingSeries.DECORATIVE,
            numberCode = "06",
            tag = "Statement",
            title = "Custom Neon Sculptures",
            description = "Hand-bent neon flex shaped into words or abstract art — the ultimate personalised light sculpture for modern living rooms.",
            imageUrl = "https://images.unsplash.com/photo-1493809842364-78817add7ffb?w=700&auto=format&fit=crop",
            idealFor = "Home bars, media rooms & teen lounges",
            colorTemp = "Warm Amber / Custom RGB"
        ),
        LightingConcept(
            id = "dec_torchiere",
            series = LightingSeries.DECORATIVE,
            numberCode = "07",
            tag = "Uplighter",
            title = "Torchière Floor Lamps",
            description = "Upward-facing bowl shades reflect ambient light off the ceiling, softening the entire room without a single harsh shadow.",
            imageUrl = "https://images.unsplash.com/photo-1600607687939-ce8a6c25118c?w=700&auto=format&fit=crop",
            idealFor = "Reading corners & sofa flanks",
            colorTemp = "2700K Diffused"
        ),
        LightingConcept(
            id = "dec_crystal",
            series = LightingSeries.DECORATIVE,
            numberCode = "08",
            tag = "Opulent",
            title = "Crystal Droplet Pendants",
            description = "Individual crystal teardrops suspended at staggered heights refract light into a thousand tiny rainbows across ceilings.",
            imageUrl = "https://images.unsplash.com/photo-1571508601891-ca5e7a713859?w=700&auto=format&fit=crop",
            idealFor = "Grand foyers & bedside drop pendants",
            colorTemp = "3000K Sparkling"
        ),

        // 03 FUNCTIONAL SPECIALTIES
        LightingConcept(
            id = "func_spotlights",
            series = LightingSeries.FUNCTIONAL,
            numberCode = "01",
            tag = "Track Focus",
            title = "Directional Spotlights",
            description = "Adjustable-angle spotlights on ceiling tracks let you redirect focus anywhere — art, shelves, or dining tables — in an instant.",
            imageUrl = null,
            idealFor = "Art walls, open shelving & kitchen islands",
            colorTemp = "3000K CRI 97"
        ),
        LightingConcept(
            id = "func_candelabra",
            series = LightingSeries.FUNCTIONAL,
            numberCode = "02",
            tag = "Evening Glow",
            title = "Candelabra Lights",
            description = "Slender flame-shaped bulbs in candelabra fittings evoke candlelight's warmth with the reliability of modern LEDs.",
            imageUrl = null,
            idealFor = "Powder rooms, sconces & heritage dining",
            colorTemp = "2200K Candlelight"
        ),
        LightingConcept(
            id = "func_grow",
            series = LightingSeries.FUNCTIONAL,
            numberCode = "03",
            tag = "Botanical",
            title = "Plant Grow Lights",
            description = "Spectrum-tuned LED discs disguised as elegant pendants keep indoor gardens lush — beauty and function in perfect balance.",
            imageUrl = null,
            idealFor = "Indoor ficus corners & herb walls",
            colorTemp = "3500K Full-Spectrum"
        ),
        LightingConcept(
            id = "func_circadian",
            series = LightingSeries.FUNCTIONAL,
            numberCode = "04",
            tag = "Wellness",
            title = "Circadian Lighting",
            description = "Tunable white LEDs shift from energising cool daylight to relaxing warm amber throughout the day, syncing with your natural rhythm.",
            imageUrl = null,
            idealFor = "Master bedrooms & work-from-home studies",
            colorTemp = "2200K–6000K Tunable"
        ),
        LightingConcept(
            id = "func_swing_arm",
            series = LightingSeries.FUNCTIONAL,
            numberCode = "05",
            tag = "No-Wire",
            title = "Plug-in Swing Arms",
            description = "Wall-mounted swing-arm lamps that plug directly into sockets — the no-wire solution for bedside reading with sculptural appeal.",
            imageUrl = null,
            idealFor = "Bedside alcoves & rental upgrades",
            colorTemp = "2700K Focused"
        ),

        // 04 SMART & INNOVATIVE
        LightingConcept(
            id = "smart_rgbw",
            series = LightingSeries.SMART,
            numberCode = "01",
            tag = "Spectrum",
            title = "RGBW Mood Lighting",
            description = "Full-spectrum RGB+White LED systems allow unlimited colour mixing — from deep amber warmth to sharp daylight white — programmable for every occasion.",
            imageUrl = null,
            idealFor = "Entertainment lounges & cove profiles",
            colorTemp = "1800K–6500K + RGB"
        ),
        LightingConcept(
            id = "smart_kinetic",
            series = LightingSeries.SMART,
            numberCode = "02",
            tag = "Choreographed",
            title = "Kinetic Chandeliers",
            description = "Motor-driven installations where individual light elements move, rise, and fall in choreographed motion — transforming a fixture into a living performance.",
            imageUrl = null,
            idealFor = "Double-height lobbies & luxury villas",
            colorTemp = "2700K Warm Dim"
        ),
        LightingConcept(
            id = "smart_infinity",
            series = LightingSeries.SMART,
            numberCode = "03",
            tag = "Optical Depth",
            title = "Mirror Infinity Lights",
            description = "Parallel mirrors facing each other with LED strips between create a tunnel of infinite, receding light — a surreal depth illusion in bathrooms and feature walls.",
            imageUrl = null,
            idealFor = "Powder rooms, dressing mirrors & bars",
            colorTemp = "3000K / Tunable"
        ),
        LightingConcept(
            id = "smart_fibre_optic",
            series = LightingSeries.SMART,
            numberCode = "04",
            tag = "Starlight",
            title = "Fibre Optic Ceilings",
            description = "Thousands of fine optical fibres woven into a ceiling panel recreate a night sky — stars twinkling in warm darkness above the bed.",
            imageUrl = null,
            idealFor = "Home theatres & master bedrooms",
            colorTemp = "2700K Twinkle"
        ),
        LightingConcept(
            id = "smart_pool",
            series = LightingSeries.SMART,
            numberCode = "05",
            tag = "Waterlight",
            title = "Underwater Pool Lights",
            description = "Colour-changing LED modules sealed into the pool shell project rippling light effects that dance across adjacent walls and ceilings at night.",
            imageUrl = null,
            idealFor = "Courtyard plunge pools & villa decks",
            colorTemp = "IP68 RGBW"
        ),
        LightingConcept(
            id = "smart_voice",
            series = LightingSeries.SMART,
            numberCode = "06",
            tag = "Automation",
            title = "Voice & App-Controlled",
            description = "Smart lighting systems integrated with Alexa, Google, or Apple HomeKit let you set scenes, dim levels, and colour temperature from anywhere.",
            imageUrl = null,
            idealFor = "Whole-home automation scenes",
            colorTemp = "Scene-Based"
        )
    )
}
